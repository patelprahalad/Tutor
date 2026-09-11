package com.vesseltutor.app.data.seed

object ScenarioCategory {
    const val STATUS_UPDATES = "Status Updates"
    const val PHONE_CALLS = "Phone Calls"
    const val MEETINGS = "Meetings"
    const val EXPLAINING_PROBLEMS = "Explaining Problems"
    const val REQUESTS = "Requests"
    const val CONFIRMATIONS = "Confirmations"

    val ALL = listOf(
        STATUS_UPDATES,
        PHONE_CALLS,
        MEETINGS,
        EXPLAINING_PROBLEMS,
        REQUESTS,
        CONFIRMATIONS
    )
}

object Difficulty {
    const val BEGINNER = "Beginner"
    const val INTERMEDIATE = "Intermediate"
    const val ADVANCED = "Advanced"
}
