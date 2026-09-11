package com.vesseltutor.app.speech

sealed class RecognitionUiState {
    data object Idle : RecognitionUiState()
    data object Listening : RecognitionUiState()
    data object Processing : RecognitionUiState()
    data class Result(val transcript: String, val uncertainWords: Set<String>) : RecognitionUiState()
    data class Error(val message: String) : RecognitionUiState()
}
