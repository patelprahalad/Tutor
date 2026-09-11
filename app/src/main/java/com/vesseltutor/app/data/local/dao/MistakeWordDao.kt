package com.vesseltutor.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vesseltutor.app.data.local.entity.MistakeWordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MistakeWordDao {

    @Query("SELECT * FROM mistake_words WHERE word = :word LIMIT 1")
    suspend fun getByWord(word: String): MistakeWordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: MistakeWordEntity)

    @Query("SELECT * FROM mistake_words ORDER BY occurrences DESC, lastSeen DESC LIMIT :limit")
    fun getTop(limit: Int): Flow<List<MistakeWordEntity>>
}
