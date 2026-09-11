package com.vesseltutor.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** A single completed practice attempt, kept for the progress dashboard and history. */
@Entity(tableName = "practice_sessions")
data class PracticeSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val scenarioId: Long,
    val category: String,
    val timestamp: Long,
    val transcribedText: String,
    val score: Int,
    val matchedPhrases: String,
    val missingPhrases: String,
    val struggledWords: String
)
