package com.vesseltutor.app.feedback

import com.vesseltutor.app.data.local.entity.ScenarioEntity

/**
 * Compares a transcript against a scenario's expected key phrases and vocabulary, and builds
 * both a short spoken summary (for TTS) and a longer text summary (for the feedback card).
 *
 * Matching is intentionally forgiving: a beginner speaker will rarely produce an expected
 * phrase word-for-word, so a phrase counts as "said" either when it appears verbatim in the
 * transcript, or when roughly half or more of its significant words show up somewhere in the
 * answer. Scoring favors encouragement over strictness — a short, on-topic answer should never
 * come back feeling like a failing grade.
 */
object FeedbackEngine {

    private const val PHRASE_WORD_MATCH_THRESHOLD = 0.5
    private const val MIN_WORDS_FOR_FULL_SCORE = 3
    private const val SHORT_ANSWER_PENALTY = 10

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
        val lengthPenalty = if (transcriptWords.size < MIN_WORDS_FOR_FULL_SCORE) SHORT_ANSWER_PENALTY else 0
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

    /**
     * Scores a "listen and repeat" attempt: how much of [target] shows up in [transcript],
     * word for word, rather than matching against a scenario's key phrases.
     */
    fun evaluateRepeat(
        transcript: String,
        target: String,
        struggledWords: Set<String>
    ): FeedbackResult {
        val transcriptWords = tokenize(transcript.lowercase())
        val targetWords = tokenize(target.lowercase())

        val matchedWords = targetWords.filter { tw ->
            transcriptWords.any { it == tw || it.contains(tw) || tw.contains(it) }
        }
        val missedWords = targetWords.filterNot { matchedWords.contains(it) }

        val score = if (targetWords.isEmpty()) 100 else matchedWords.size * 100 / targetWords.size
        val struggled = struggledWords.filter { it.isNotBlank() }.distinct()

        return FeedbackResult(
            score = score,
            matchedPhrases = matchedWords,
            missingPhrases = missedWords,
            matchedVocabulary = emptyList(),
            struggledWords = struggled,
            spokenFeedback = buildRepeatSpokenFeedback(score, missedWords, struggled),
            summaryText = buildRepeatSummaryText(score, missedWords, struggled)
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
            score >= 50 -> "Good attempt, you're on the right track."
            else -> "Nice try — this gets easier with practice."
        }

        val strengths = if (matched.isNotEmpty()) {
            "You clearly included ${matched.take(2).joinToString(" and ")}."
        } else ""

        val improvement = if (missing.isNotEmpty()) {
            "Next time, try to also say something like: ${missing.first()}."
        } else "You covered all the key points."

        val pronunciation = if (struggled.isNotEmpty()) {
            "The recognizer wasn't fully sure about the word ${struggled.first()}, so it may be worth practicing that one slowly."
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

    private fun buildRepeatSpokenFeedback(score: Int, missed: List<String>, struggled: List<String>): String {
        val opener = when {
            score >= 85 -> "Great repeating!"
            score >= 60 -> "Nice, pretty close."
            else -> "Let's try that sentence again, a little slower."
        }
        val missedPart = if (missed.isNotEmpty()) {
            "You missed the word ${missed.first()}."
        } else {
            "You said every word."
        }
        val pronunciation = if (struggled.isNotEmpty()) {
            "Try saying ${struggled.first()} more clearly."
        } else ""

        return listOf(opener, missedPart, pronunciation).filter { it.isNotBlank() }.joinToString(" ")
    }

    private fun buildRepeatSummaryText(score: Int, missed: List<String>, struggled: List<String>): String {
        val builder = StringBuilder()
        builder.append("Match: $score / 100\n")
        if (missed.isNotEmpty()) {
            builder.append("Words you missed: ${missed.joinToString(", ")}\n")
        }
        if (struggled.isNotEmpty()) {
            builder.append("Practice pronouncing: ${struggled.joinToString(", ")}")
        }
        return builder.toString().trim()
    }
}
