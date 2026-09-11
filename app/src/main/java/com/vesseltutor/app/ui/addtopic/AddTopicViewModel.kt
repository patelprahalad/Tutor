package com.vesseltutor.app.ui.addtopic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vesseltutor.app.data.ApiKeyStore
import com.vesseltutor.app.data.local.entity.ScenarioEntity
import com.vesseltutor.app.data.repository.ScenarioRepository
import com.vesseltutor.app.network.AnthropicClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddTopicViewModel(
    private val anthropicClient: AnthropicClient,
    private val scenarioRepository: ScenarioRepository,
    apiKeyStore: ApiKeyStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTopicUiState())
    val uiState: StateFlow<AddTopicUiState> = _uiState.asStateFlow()

    val hasApiKey: StateFlow<Boolean> = apiKeyStore.apiKey
        .map { it.isNotBlank() }
        .stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), apiKeyStore.hasKey())

    fun generateFromTopic(topic: String) {
        if (topic.isBlank()) return
        runGeneration { anthropicClient.generateFromTopic(topic.trim()) }
    }

    fun generateFreshTopic() {
        runGeneration { anthropicClient.generateFreshTopic() }
    }

    fun generateFromImage(bytes: ByteArray, mimeType: String) {
        runGeneration { anthropicClient.generateFromImage(bytes, mimeType) }
    }

    fun generateFromDocumentText(text: String) {
        if (text.isBlank()) return
        runGeneration { anthropicClient.generateFromDocumentText(text) }
    }

    private fun runGeneration(block: suspend () -> ScenarioEntity) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null, createdScenarioId = null) }
        viewModelScope.launch {
            try {
                val scenario = block()
                val id = scenarioRepository.saveGenerated(scenario)
                _uiState.update { it.copy(isLoading = false, createdScenarioId = id) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message ?: "Something went wrong. Please try again.")
                }
            }
        }
    }

    fun consumeCreatedScenario() {
        _uiState.update { it.copy(createdScenarioId = null) }
    }

    fun dismissError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
