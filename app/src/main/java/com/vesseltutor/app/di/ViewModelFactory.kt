package com.vesseltutor.app.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vesseltutor.app.VesselTutorApp
import com.vesseltutor.app.ui.library.LibraryViewModel
import com.vesseltutor.app.ui.practice.PracticeViewModel
import com.vesseltutor.app.ui.progress.ProgressViewModel

class ViewModelFactory(private val app: VesselTutorApp) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(PracticeViewModel::class.java) ->
            PracticeViewModel(app, app.container.scenarioRepository, app.container.progressRepository) as T

        modelClass.isAssignableFrom(LibraryViewModel::class.java) ->
            LibraryViewModel(app.container.scenarioRepository) as T

        modelClass.isAssignableFrom(ProgressViewModel::class.java) ->
            ProgressViewModel(app.container.progressRepository) as T

        else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
