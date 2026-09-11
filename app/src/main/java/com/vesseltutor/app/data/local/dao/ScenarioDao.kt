package com.vesseltutor.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vesseltutor.app.data.local.entity.ScenarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScenarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(scenarios: List<ScenarioEntity>)

    @Query("SELECT * FROM scenarios ORDER BY category, difficulty")
    fun getAll(): Flow<List<ScenarioEntity>>

    @Query("SELECT * FROM scenarios WHERE category = :category ORDER BY difficulty")
    fun getByCategory(category: String): Flow<List<ScenarioEntity>>

    @Query("SELECT * FROM scenarios")
    suspend fun getAllOnce(): List<ScenarioEntity>

    @Query("SELECT * FROM scenarios WHERE category = :category")
    suspend fun getByCategoryOnce(category: String): List<ScenarioEntity>

    @Query("SELECT * FROM scenarios WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): ScenarioEntity?

    @Query("SELECT COUNT(*) FROM scenarios")
    suspend fun getCount(): Int
}
