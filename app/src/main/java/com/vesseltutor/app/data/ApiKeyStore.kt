package com.vesseltutor.app.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Stores the user's own Anthropic API key encrypted at rest, local to this device only. */
class ApiKeyStore(context: Context) {

    private val prefs = run {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            "vessel_tutor_secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private val _apiKey = MutableStateFlow(prefs.getString(KEY_API_KEY, null).orEmpty())
    val apiKey: StateFlow<String> = _apiKey.asStateFlow()

    fun save(key: String) {
        prefs.edit().putString(KEY_API_KEY, key).apply()
        _apiKey.value = key
    }

    fun clear() {
        prefs.edit().remove(KEY_API_KEY).apply()
        _apiKey.value = ""
    }

    fun hasKey(): Boolean = _apiKey.value.isNotBlank()

    private companion object {
        const val KEY_API_KEY = "anthropic_api_key"
    }
}
