package com.vesseltutor.app.data.repository

import com.vesseltutor.app.data.local.dao.MistakeWordDao
import com.vesseltutor.app.data.local.dao.PracticeSessionDao
import com.vesseltutor.app.data.local.dao.StreakDao
import com.vesseltutor.app.data.local.entity.MistakeWordEntity
import com.vesseltutor.app.data.local.entity.PracticeSessionEntity
import com.vesseltutor.app.data.local.entity.StreakEntity
import com.vesseltutor.app.feedback.FeedbackResult
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId

class ProgressRepository(
    private val sessionDao: PracticeSessionDao,
    private val mistakeWordDao: MistakeWordDao,
    private val streakDao: StreakDao
) {

    suspend fun recordSession(scenarioId: Long, category: String, transcript: String, feedback: FeedbackResult) {
        sessionDao.insert(
            PracticeSessionEntity(
                scenarioId = scenarioId,
                category = category,
                timestamp = System.currentTimeMillis(),
                transcribedText = transcript,
                score = feedback.score,
                matchedPhrases = feedback.matchedPhrases.joinToString("|"),
                missingPhrases = feedback.missingPhrases.joinToString("|"),
                struggledWords = feedback.struggledWords.joinToString("|")
            )
        )
    }

    suspend fun recordStruggledWords(words: Set<String>) {
        val now = System.currentTimeMillis()
        words.filter { it.isNotBlank() }.forEach { word ->
            val existing = mistakeWordDao.getByWord(word)
            mistakeWordDao.upsert(
                existing?.copy(occurrences = existing.occurrences + 1, lastSeen = now)
                    ?: MistakeWordEntity(word = word, occurrences = 1, lastSeen = now)
            )
        }
    }

    suspend fun updateStreakForToday() {
        val today = LocalDate.now(ZoneId.systemDefault()).toEpochDay()
        val current = streakDao.getOnce()

        val newStreak = when {
            current == null -> 1
            current.lastPracticeEpochDay == today -> current.currentStreak
            current.lastPracticeEpochDay == today - 1 -> current.currentStreak + 1
            else -> 1
        }

        val totalSessions = (current?.totalSessions ?: 0) + 1
        val longest = maxOf(current?.longestStreak ?: 0, newStreak)

        streakDao.upsert(
            StreakEntity(
                currentStreak = newStreak,
                longestStreak = longest,
                lastPracticeEpochDay = today,
                totalSessions = totalSessions
            )
        )
    }

    fun streakFlow(): Flow<StreakEntity?> = streakDao.observe()

    fun recentSessionsFlow(limit: Int = 20): Flow<List<PracticeSessionEntity>> = sessionDao.getRecent(limit)

    fun topMistakesFlow(limit: Int = 10): Flow<List<MistakeWordEntity>> = mistakeWordDao.getTop(limit)

    suspend fun getAverageScore(): Double? = sessionDao.getAverageScore()

    suspend fun getTotalSessions(): Int = sessionDao.countAll()
}
