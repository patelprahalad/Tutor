package com.vesseltutor.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Singleton row (fixed [id] = 1) holding the user's practice streak and lifetime totals. */
@Entity(tableName = "streak")
data class StreakEntity(
    @PrimaryKey val id: Int = 1,
    val currentStreak: Int,
    val longestStreak: Int,
    val lastPracticeEpochDay: Long,
    val totalSessions: Int
)
