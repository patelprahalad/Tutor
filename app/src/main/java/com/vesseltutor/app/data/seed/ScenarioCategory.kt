package com.vesseltutor.app.data.seed

object ScenarioCategory {
    const val STATUS_UPDATES = "Status Updates"
    const val PHONE_CALLS = "Phone Calls"
    const val MEETINGS = "Meetings"
    const val EXPLAINING_PROBLEMS = "Explaining Problems"
    const val REQUESTS = "Requests"
    const val CONFIRMATIONS = "Confirmations"
    const val DAILY_LIFE = "Daily Life"
    /** Holds scenarios generated from a user-typed topic, photo, or uploaded document. */
    const val MY_TOPICS = "My Topics"

    val SEEDED = listOf(
        STATUS_UPDATES,
        PHONE_CALLS,
        MEETINGS,
        EXPLAINING_PROBLEMS,
        REQUESTS,
        CONFIRMATIONS,
        DAILY_LIFE
    )

    val ALL = SEEDED + MY_TOPICS
}

object Difficulty {
    const val BEGINNER = "Beginner"
    const val INTERMEDIATE = "Intermediate"
    const val ADVANCED = "Advanced"
}
