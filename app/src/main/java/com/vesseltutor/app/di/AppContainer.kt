package com.vesseltutor.app.di

import android.content.Context
import com.vesseltutor.app.data.ApiKeyStore
import com.vesseltutor.app.data.local.AppDatabase
import com.vesseltutor.app.data.repository.ProgressRepository
import com.vesseltutor.app.data.repository.ScenarioRepository
import com.vesseltutor.app.network.AnthropicClient

/** Minimal hand-rolled service locator; swap for Hilt later if the app grows. */
class AppContainer(context: Context) {

    private val database: AppDatabase by lazy { AppDatabase.getInstance(context) }

    val scenarioRepository: ScenarioRepository by lazy { ScenarioRepository(database.scenarioDao()) }

    val progressRepository: ProgressRepository by lazy {
        ProgressRepository(database.sessionDao(), database.mistakeWordDao(), database.streakDao())
    }

    val apiKeyStore: ApiKeyStore by lazy { ApiKeyStore(context) }

    val anthropicClient: AnthropicClient by lazy { AnthropicClient(apiKeyStore) }
}
