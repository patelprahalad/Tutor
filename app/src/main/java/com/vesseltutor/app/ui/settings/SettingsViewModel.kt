package com.vesseltutor.app.ui.settings

import androidx.lifecycle.ViewModel
import com.vesseltutor.app.data.ApiKeyStore
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(private val apiKeyStore: ApiKeyStore) : ViewModel() {

    val apiKey: StateFlow<String> = apiKeyStore.apiKey

    fun save(key: String) {
        apiKeyStore.save(key.trim())
    }

    fun clear() {
        apiKeyStore.clear()
    }
}
