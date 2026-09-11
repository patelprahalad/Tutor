package com.vesseltutor.app.speech

/**
 * Android's [android.speech.SpeechRecognizer] does not expose true per-word confidence.
 * As a rough proxy for pronunciation trouble, we request several alternate transcriptions
 * (EXTRA_MAX_RESULTS) and flag words in the top hypothesis that do not appear in every
 * alternate hypothesis — those are the words the recognizer was least sure about.
 */
object WordUncertaintyAnalyzer {

    fun findUncertainWords(hypotheses: List<String>): Set<String> {
        if (hypotheses.size < 2) return emptySet()

        val topWords = hypotheses.first().lowercase().split(Regex("\\s+")).filter { it.isNotBlank() }
        val alternateWordSets = hypotheses.drop(1).map { hypothesis ->
            hypothesis.lowercase().split(Regex("\\s+")).filter { it.isNotBlank() }.toSet()
        }

        return topWords.filter { word -> alternateWordSets.any { !it.contains(word) } }.toSet()
    }
}
