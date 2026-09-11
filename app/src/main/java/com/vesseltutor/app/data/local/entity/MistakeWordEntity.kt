package com.vesseltutor.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tracks a word the on-device speech recognizer repeatedly struggled with, used as a rough
 * proxy for pronunciation issues since [android.speech.SpeechRecognizer] exposes no true
 * per-word confidence.
 */
@Entity(tableName = "mistake_words")
data class MistakeWordEntity(
    @PrimaryKey val word: String,
    val occurrences: Int,
    val lastSeen: Long
)
