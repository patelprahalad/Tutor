package com.vesseltutor.app.feedback

/** Result of comparing a transcribed answer against a scenario's expected content. */
data class FeedbackResult(
    val score: Int,
    val matchedPhrases: List<String>,
    val missingPhrases: List<String>,
    val matchedVocabulary: List<String>,
    val struggledWords: List<String>,
    val spokenFeedback: String,
    val summaryText: String
)
