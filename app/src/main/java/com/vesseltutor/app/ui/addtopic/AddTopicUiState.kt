package com.vesseltutor.app.ui.addtopic

data class AddTopicUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val createdScenarioId: Long? = null
)
