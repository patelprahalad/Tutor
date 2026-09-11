package com.vesseltutor.app.feedback

import com.vesseltutor.app.data.local.entity.ScenarioEntity

/**
 * Compares a transcript against a scenario's expected key phrases and vocabulary, and builds
 * both a short spoken summary (for TTS) and a longer text summary (for the feedback card).
 *
 * Matching is intentionally forgiving: a beginner speaker will rarely produce an expected
 * phrase word-for-word, so a phrase counts as "said" either when it appears verbatim in the
 * transcript, or when most of its significant words show up somewhere in the answer.
 */
object FeedbackEngine {

    private const val PHRASE_WORD_MATCH_THRESHOLD = 0.7
    private const val MIN_WORDS_FOR_FULL_SCORE = 4

    fun evaluate(
        transcript: String,
        scenario: ScenarioEntity,
        struggledWords: Set<String>
    ): FeedbackResult {
        val normalizedTranscript = transcript.lowercase().trim()
        val transcriptWords = tokenize(normalizedTranscript)

        val keyPhrases = scenario.keyPhraseList()
        val vocabulary = scenario.vocabularyList()

        val matchedPhrases = keyPhrases.filter { phraseMatches(normalizedTranscript, transcriptWords, it) }
        val missingPhrases = keyPhrases.filterNot { matchedPhrases.contains(it) }
        val matchedVocabulary = vocabulary.filter { normalizedTranscript.contains(it.lowercase()) }

        val phraseScore = if (keyPhrases.isEmpty()) 100 else matchedPhrases.size * 100 / keyPhrases.size
        val vocabScore = if (vocabulary.isEmpty()) 100 else matchedVocabulary.size * 100 / vocabulary.size
        val lengthPenalty = if (transcriptWords.size < MIN_WORDS_FOR_FULL_SCORE) 20 else 0
        val score = ((phraseScore * 0.7 + vocabScore * 0.3).toInt() - lengthPenalty).coerceIn(0, 100)

        val struggled = struggledWords.filter { it.isNotBlank() }.distinct()

        return FeedbackResult(
            score = score,
            matchedPhrases = matchedPhrases,
            missingPhrases = missingPhrases,
            matchedVocabulary = matchedVocabulary,
            struggledWords = struggled,
            spokenFeedback = buildSpokenFeedback(score, matchedPhrases, missingPhrases, struggled),
            summaryText = buildSummaryText(score, matchedPhrases, missingPhrases, matchedVocabulary)
        )
    }

    private fun tokenize(text: String): Set<String> =
        text.split(Regex("\\s+"))
            .map { it.trim('.', ',', '!', '?', ':', ';') }
            .filter { it.isNotBlank() }
            .toSet()

    private fun phraseMatches(transcript: String, transcriptWords: Set<String>, phrase: String): Boolean {
        val phraseLower = phrase.lowercase()
        if (transcript.contains(phraseLower)) return true

        val significantWords = phraseLower.split(Regex("\\s+")).filter { it.length > 2 }
        if (significantWords.isEmpty()) return false

        val hits = significantWords.count { pw -> transcriptWords.any { w -> w == pw || w.contains(pw) || pw.contains(w) } }
        return hits.toDouble() / significantWords.size >= PHRASE_WORD_MATCH_THRESHOLD
    }

    private fun buildSpokenFeedback(
        score: Int,
        matched: List<String>,
        missing: List<String>,
        struggled: List<String>
    ): String {
        val opener = when {
            score >= 80 -> "Well done."
            score >= 50 -> "Good attempt."
            else -> "Let's keep practicing."
        }

        val strengths = if (matched.isNotEmpty()) {
            "You clearly included ${matched.take(2).joinToString(" and ")}."
        } else ""

        val improvement = if (missing.isNotEmpty()) {
            "Next time, try to also say something like: ${missing.first()}."
        } else "You covered all the key points."

        val pronunciation = if (struggled.isNotEmpty()) {
            "The recognizer had trouble with the word ${struggled.first()}, so it may be worth practicing that word slowly."
        } else ""

        return listOf(opener, strengths, improvement, pronunciation)
            .filter { it.isNotBlank() }
            .joinToString(" ")
    }

    private fun buildSummaryText(
        score: Int,
        matched: List<String>,
        missing: List<String>,
        matchedVocabulary: List<String>
    ): String {
        val builder = StringBuilder()
        builder.append("Score: $score / 100\n")
        if (matched.isNotEmpty()) {
            builder.append("Well said: ${matched.joinToString(", ")}\n")
        }
        if (missing.isNotEmpty()) {
            builder.append("Try to include: ${missing.joinToString(", ")}\n")
        }
        if (matchedVocabulary.isNotEmpty()) {
            builder.append("Good vocabulary use: ${matchedVocabulary.joinToString(", ")}")
        }
        return builder.toString().trim()
    }
}
