package com.vesseltutor.app.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

/**
 * Wraps [SpeechRecognizer] with generous silence-detection timeouts so a hesitant,
 * non-native speaker is not cut off, and exposes recognition state as a [StateFlow] so the
 * UI can distinguish "listening" from "processing" from "result ready".
 */
class SpeechRecognizerManager(private val context: Context) {

    private var recognizer: SpeechRecognizer? = null

    private val _state = MutableStateFlow<RecognitionUiState>(RecognitionUiState.Idle)
    val state: StateFlow<RecognitionUiState> = _state.asStateFlow()

    fun startListening() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _state.value = RecognitionUiState.Error("Speech recognition is not available on this device.")
            return
        }

        stopListening()

        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString())
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
            // Generous silence windows so a slow or hesitant speaker isn't cut off early.
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 3000L)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLE_COMPLETE_SILENCE_LENGTH_MILLIS, 3000L)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 15000L)
        }

        val newRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        recognizer = newRecognizer
        newRecognizer.setRecognitionListener(createListener())
        newRecognizer.startListening(recognizerIntent)
    }

    fun stopListening() {
        recognizer?.destroy()
        recognizer = null
    }

    fun reset() {
        _state.value = RecognitionUiState.Idle
    }

    fun destroy() {
        stopListening()
    }

    private fun createListener() = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            _state.value = RecognitionUiState.Listening
        }

        override fun onBeginningOfSpeech() {
            _state.value = RecognitionUiState.Listening
        }

        override fun onRmsChanged(rmsdB: Float) {}

        override fun onBufferReceived(buffer: ByteArray?) {}

        override fun onEndOfSpeech() {
            _state.value = RecognitionUiState.Processing
        }

        override fun onError(error: Int) {
            _state.value = RecognitionUiState.Error(errorMessage(error))
        }

        override fun onResults(results: Bundle) {
            val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION).orEmpty()
            val transcript = matches.firstOrNull()?.trim().orEmpty()

            _state.value = if (transcript.isBlank()) {
                RecognitionUiState.Error("I didn't catch that. Please try again.")
            } else {
                RecognitionUiState.Result(transcript, WordUncertaintyAnalyzer.findUncertainWords(matches))
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {}

        override fun onEvent(eventType: Int, params: Bundle?) {}
    }

    private fun errorMessage(error: Int): String = when (error) {
        SpeechRecognizer.ERROR_NO_MATCH -> "I didn't catch that. Please try speaking again."
        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech detected. Tap the microphone and try again when ready."
        SpeechRecognizer.ERROR_AUDIO -> "There was an audio recording error. Please try again."
        SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT ->
            "A network error occurred during recognition."
        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission is required."
        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "The recognizer is busy, please wait a moment and try again."
        SpeechRecognizer.ERROR_CLIENT -> "Recognition was cancelled. Tap the microphone to try again."
        else -> "Something went wrong. Please try again."
    }
}
