package com.vesseltutor.app.ui.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vesseltutor.app.data.local.entity.MistakeWordEntity
import com.vesseltutor.app.data.local.entity.PracticeSessionEntity
import com.vesseltutor.app.data.local.entity.StreakEntity
import com.vesseltutor.app.data.repository.ProgressRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ProgressViewModel(private val repository: ProgressRepository) : ViewModel() {

    val streak: StateFlow<StreakEntity?> =
        repository.streakFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val recentSessions: StateFlow<List<PracticeSessionEntity>> =
        repository.recentSessionsFlow(30).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val topMistakes: StateFlow<List<MistakeWordEntity>> =
        repository.topMistakesFlow(10).stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
