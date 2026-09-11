package com.vesseltutor.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vesseltutor.app.data.local.entity.StreakEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StreakDao {

    @Query("SELECT * FROM streak WHERE id = 1 LIMIT 1")
    fun observe(): Flow<StreakEntity?>

    @Query("SELECT * FROM streak WHERE id = 1 LIMIT 1")
    suspend fun getOnce(): StreakEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: StreakEntity)
}
