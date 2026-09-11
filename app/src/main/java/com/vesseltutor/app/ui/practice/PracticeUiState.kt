package com.vesseltutor.app.ui.practice

import com.vesseltutor.app.data.local.entity.ScenarioEntity
import com.vesseltutor.app.feedback.FeedbackResult

enum class Phase { IDLE, LISTENING, PROCESSING, FEEDBACK_READY }

enum class PracticeMode { FREE_RESPONSE, REPEAT_AFTER_ME }

data class PracticeUiState(
    val scenario: ScenarioEntity? = null,
    val isLoadingScenario: Boolean = true,
    val mode: PracticeMode = PracticeMode.FREE_RESPONSE,
    val phase: Phase = Phase.IDLE,
    val transcript: String = "",
    val struggledWords: Set<String> = emptySet(),
    val feedback: FeedbackResult? = null,
    val errorMessage: String? = null,
    val streak: Int = 0,
    val isTtsSpeaking: Boolean = false,
    val hasMicPermission: Boolean = false
)
