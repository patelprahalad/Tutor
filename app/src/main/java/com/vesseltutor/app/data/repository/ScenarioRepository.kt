package com.vesseltutor.app.data.repository

import com.vesseltutor.app.data.local.dao.ScenarioDao
import com.vesseltutor.app.data.local.entity.ScenarioEntity
import com.vesseltutor.app.data.seed.ScenarioSeeder
import kotlinx.coroutines.flow.Flow

class ScenarioRepository(private val dao: ScenarioDao) {

    suspend fun seedIfEmpty() {
        if (dao.getCount() == 0) {
            dao.insertAll(ScenarioSeeder.seedScenarios())
        }
    }

    fun getAll(): Flow<List<ScenarioEntity>> = dao.getAll()

    fun getByCategory(category: String): Flow<List<ScenarioEntity>> = dao.getByCategory(category)

    suspend fun getById(id: Long): ScenarioEntity? = dao.getById(id)

    suspend fun getRandomScenario(category: String? = null): ScenarioEntity? {
        val list = if (category != null) dao.getByCategoryOnce(category) else dao.getAllOnce()
        return list.randomOrNull()
    }
}
