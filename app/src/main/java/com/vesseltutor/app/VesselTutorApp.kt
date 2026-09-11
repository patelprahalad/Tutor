package com.vesseltutor.app

import android.app.Application
import com.vesseltutor.app.di.AppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VesselTutorApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        CoroutineScope(Dispatchers.IO).launch {
            container.scenarioRepository.seedIfEmpty()
        }
    }
}
