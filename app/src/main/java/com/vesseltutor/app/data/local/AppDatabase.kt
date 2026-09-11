package com.vesseltutor.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vesseltutor.app.data.local.dao.MistakeWordDao
import com.vesseltutor.app.data.local.dao.PracticeSessionDao
import com.vesseltutor.app.data.local.dao.ScenarioDao
import com.vesseltutor.app.data.local.dao.StreakDao
import com.vesseltutor.app.data.local.entity.MistakeWordEntity
import com.vesseltutor.app.data.local.entity.PracticeSessionEntity
import com.vesseltutor.app.data.local.entity.ScenarioEntity
import com.vesseltutor.app.data.local.entity.StreakEntity

@Database(
    entities = [
        ScenarioEntity::class,
        PracticeSessionEntity::class,
        MistakeWordEntity::class,
        StreakEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scenarioDao(): ScenarioDao
    abstract fun sessionDao(): PracticeSessionDao
    abstract fun mistakeWordDao(): MistakeWordDao
    abstract fun streakDao(): StreakDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "vessel_tutor.db"
                ).build().also { INSTANCE = it }
            }
    }
}
