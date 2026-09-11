package com.vesseltutor.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A single practice scenario. [keyPhrases] and [vocabulary] are stored as "|"-separated
 * strings so Room can persist them as plain TEXT columns without a type converter.
 */
@Entity(tableName = "scenarios")
data class ScenarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String,
    val difficulty: String,
    /** Short situational setup shown and spoken before the prompt, e.g. "You're on the bridge...". */
    val context: String = "",
    val promptText: String,
    val keyPhrases: String,
    val vocabulary: String,
    val sampleAnswer: String,
    /** True for scenarios a user generated from a topic, photo or document, rather than seeded content. */
    val isUserGenerated: Boolean = false
) {
    fun keyPhraseList(): List<String> =
        keyPhrases.split("|").map { it.trim() }.filter { it.isNotBlank() }

    fun vocabularyList(): List<String> =
        vocabulary.split("|").map { it.trim() }.filter { it.isNotBlank() }
}
