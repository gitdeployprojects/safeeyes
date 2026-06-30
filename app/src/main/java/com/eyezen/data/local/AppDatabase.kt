package com.eyezen.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.eyezen.data.local.converter.DateTimeConverter
import com.eyezen.data.local.dao.BreakDao
import com.eyezen.data.local.dao.UserPreferencesDao
import com.eyezen.data.local.dao.WaterIntakeDao
import com.eyezen.data.local.entity.BreakEntity
import com.eyezen.data.local.entity.UserPreferencesEntity
import com.eyezen.data.local.entity.WaterIntakeEntity

/**
 * EyeZen Room Database.
 *
 * Contains entities for:
 * - Breaks (eye care sessions)
 * - User Preferences
 * - Water Intake tracking
 *
 * Version: 1
 */
@Database(
    entities = [
        BreakEntity::class,
        UserPreferencesEntity::class,
        WaterIntakeEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTimeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun breakDao(): BreakDao
    abstract fun userPreferencesDao(): UserPreferencesDao
    abstract fun waterIntakeDao(): WaterIntakeDao
}
