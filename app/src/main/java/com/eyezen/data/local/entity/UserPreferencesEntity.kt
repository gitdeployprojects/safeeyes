package com.eyezen.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * User preferences entity.
 *
 * Stores user settings and preferences.
 */
@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey
    val userId: String,
    val theme: String = "system", // "light", "dark", "system"
    val language: String = "en", // ISO 639-1 code
    val breakDuration: Int = 20, // seconds
    val breakInterval: Int = 20, // minutes
    val enableNotifications: Boolean = true,
    val notificationSound: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val dailyWaterGoal: Int = 8, // number of glasses
    val timeZone: String = "UTC",
    val premiumStatus: Boolean = false
)
