package com.vesseltutor.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vesseltutor.app.data.local.entity.PracticeSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PracticeSessionDao {

    @Insert
    suspend fun insert(session: PracticeSessionEntity): Long

    @Query("SELECT * FROM practice_sessions ORDER BY timestamp DESC")
    fun getAll(): Flow<List<PracticeSessionEntity>>

    @Query("SELECT * FROM practice_sessions ORDER BY timestamp DESC LIMIT :limit")
    fun getRecent(limit: Int): Flow<List<PracticeSessionEntity>>

    @Query("SELECT COUNT(*) FROM practice_sessions")
    suspend fun countAll(): Int

    @Query("SELECT AVG(score) FROM practice_sessions")
    suspend fun getAverageScore(): Double?
}
