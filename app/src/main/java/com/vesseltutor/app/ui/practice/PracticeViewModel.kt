package com.vesseltutor.app.ui.practice

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vesseltutor.app.data.local.entity.ScenarioEntity
import com.vesseltutor.app.data.repository.ProgressRepository
import com.vesseltutor.app.data.repository.ScenarioRepository
import com.vesseltutor.app.feedback.FeedbackEngine
import com.vesseltutor.app.speech.RecognitionUiState
import com.vesseltutor.app.speech.SpeechRecognizerManager
import com.vesseltutor.app.speech.TextToSpeechManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PracticeViewModel(
    app: Application,
    private val scenarioRepository: ScenarioRepository,
    private val progressRepository: ProgressRepository
) : AndroidViewModel(app) {

    private val ttsManager = TextToSpeechManager(app)
    private val speechManager = SpeechRecognizerManager(app)

    private val _uiState = MutableStateFlow(PracticeUiState())
    val uiState: StateFlow<PracticeUiState> = _uiState.asStateFlow()

    private var initialized = false

    init {
        viewModelScope.launch {
            speechManager.state.collect { handleRecognitionState(it) }
        }
        viewModelScope.launch {
            ttsManager.isSpeaking.collect { speaking -> _uiState.update { it.copy(isTtsSpeaking = speaking) } }
        }
        viewModelScope.launch {
            progressRepository.streakFlow().collect { streak ->
                _uiState.update { it.copy(streak = streak?.currentStreak ?: 0) }
            }
        }
    }

    /** Called once from the screen with any scenario id passed via navigation. */
    fun initialize(scenarioId: Long?) {
        if (initialized) return
        initialized = true
        if (scenarioId != null) loadScenarioById(scenarioId) else loadRandomScenario(null)
    }

    fun loadRandomScenario(category: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingScenario = true) }
            applyScenario(scenarioRepository.getRandomScenario(category))
        }
    }

    private fun loadScenarioById(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingScenario = true) }
            applyScenario(scenarioRepository.getById(id))
        }
    }

    private fun applyScenario(scenario: ScenarioEntity?) {
        _uiState.update {
            it.copy(
                scenario = scenario,
                phase = Phase.IDLE,
                transcript = "",
                feedback = null,
                struggledWords = emptySet(),
                errorMessage = null,
                isLoadingScenario = false
            )
        }
    }

    fun speakPrompt() {
        uiState.value.scenario?.let { ttsManager.speak(it.promptText) }
    }

    fun onMicPermissionResult(granted: Boolean) {
        _uiState.update { it.copy(hasMicPermission = granted) }
    }

    fun startListening() {
        if (!_uiState.value.hasMicPermission) return
        ttsManager.stop()
        _uiState.update { it.copy(phase = Phase.LISTENING, errorMessage = null) }
        speechManager.startListening()
    }

    fun cancelListening() {
        speechManager.stopListening()
        _uiState.update { it.copy(phase = Phase.IDLE) }
    }

    private fun handleRecognitionState(state: RecognitionUiState) {
        when (state) {
            is RecognitionUiState.Idle -> Unit
            is RecognitionUiState.Listening -> _uiState.update { it.copy(phase = Phase.LISTENING) }
            is RecognitionUiState.Processing -> _uiState.update { it.copy(phase = Phase.PROCESSING) }
            is RecognitionUiState.Result -> onTranscriptReady(state.transcript, state.uncertainWords)
            is RecognitionUiState.Error -> _uiState.update {
                it.copy(phase = Phase.IDLE, errorMessage = state.message)
            }
        }
    }

    private fun onTranscriptReady(transcript: String, uncertainWords: Set<String>) {
        val scenario = _uiState.value.scenario ?: return
        val feedback = FeedbackEngine.evaluate(transcript, scenario, uncertainWords)

        _uiState.update {
            it.copy(
                transcript = transcript,
                struggledWords = uncertainWords,
                feedback = feedback,
                phase = Phase.FEEDBACK_READY
            )
        }

        ttsManager.speak(feedback.spokenFeedback)

        viewModelScope.launch {
            progressRepository.recordSession(scenario.id, scenario.category, transcript, feedback)
            progressRepository.recordStruggledWords(uncertainWords)
            progressRepository.updateStreakForToday()
        }
    }

    fun tryAgain() {
        speechManager.reset()
        _uiState.update {
            it.copy(
                phase = Phase.IDLE,
                transcript = "",
                feedback = null,
                struggledWords = emptySet(),
                errorMessage = null
            )
        }
    }

    fun nextScenario() {
        loadRandomScenario(_uiState.value.scenario?.category)
    }

    fun dismissError() {
        speechManager.reset()
        _uiState.update { it.copy(errorMessage = null, phase = Phase.IDLE) }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
        speechManager.destroy()
    }
}
