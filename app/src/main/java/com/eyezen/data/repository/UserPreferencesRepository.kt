package com.eyezen.data.repository

import com.eyezen.data.local.dao.UserPreferencesDao
import com.eyezen.data.local.entity.UserPreferencesEntity
import com.eyezen.data.remote.SupabaseClient
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

/**
 * User preferences repository.
 *
 * Manages user settings and preferences:
 * - Theme, language, notifications
 * - Break intervals and durations
 * - Cloud sync
 */
class UserPreferencesRepository(
    private val userPreferencesDao: UserPreferencesDao,
    private val supabaseClient: SupabaseClient
) {

    /**
     * Get user preferences as flow
     */
    fun getPreferences(userId: String): Flow<UserPreferencesEntity?> {
        return userPreferencesDao.getPreferences(userId)
    }

    /**
     * Update user preferences
     */
    suspend fun updatePreferences(preferences: UserPreferencesEntity): Result<Unit> {
        return try {
            userPreferencesDao.update(preferences)
            Timber.d("Preferences updated for ${preferences.userId}")
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Failed to update preferences")
            Result.failure(e)
        }
    }

    /**
     * Initialize default preferences for new user
     */
    suspend fun initializePreferences(userId: String): Result<UserPreferencesEntity> {
        return try {
            val preferences = UserPreferencesEntity(userId = userId)
            userPreferencesDao.insert(preferences)
            Timber.d("Preferences initialized for $userId")
            Result.success(preferences)
        } catch (e: Exception) {
            Timber.e(e, "Failed to initialize preferences")
            Result.failure(e)
        }
    }
}
