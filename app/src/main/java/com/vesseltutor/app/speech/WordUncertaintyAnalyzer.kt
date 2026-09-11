package com.vesseltutor.app.speech

/**
 * Android's [android.speech.SpeechRecognizer] does not expose true per-word confidence.
 * As a rough proxy for pronunciation trouble, we request several alternate transcriptions
 * (EXTRA_MAX_RESULTS) and flag words in the top hypothesis that most alternates disagree on.
 *
 * This is intentionally a majority vote rather than "any alternate disagrees" — the first
 * version of this flagged almost every word, since alternates commonly differ on at least one
 * word even for a clean recognition. Requiring disagreement from *most* alternates makes the
 * highlighted words a much stronger signal.
 */
object WordUncertaintyAnalyzer {

    private const val MIN_ALTERNATES = 2

    fun findUncertainWords(hypotheses: List<String>): Set<String> {
        val alternateWordSets = hypotheses.drop(1).map { hypothesis ->
            hypothesis.lowercase().split(Regex("\\s+")).filter { it.isNotBlank() }.toSet()
        }
        if (alternateWordSets.size < MIN_ALTERNATES) return emptySet()

        val topWords = hypotheses.first().lowercase().split(Regex("\\s+")).filter { it.isNotBlank() }

        return topWords.filter { word ->
            val missingCount = alternateWordSets.count { !it.contains(word) }
            missingCount > alternateWordSets.size / 2
        }.toSet()
    }
}
