package com.eyezen.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.eyezen.data.local.entity.UserPreferencesEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for UserPreferences entities.
 */
@Dao
interface UserPreferencesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(preferences: UserPreferencesEntity)

    @Update
    suspend fun update(preferences: UserPreferencesEntity)

    @Query("SELECT * FROM user_preferences WHERE userId = :userId")
    fun getPreferences(userId: String): Flow<UserPreferencesEntity?>

    @Query("SELECT * FROM user_preferences WHERE userId = :userId")
    suspend fun getPreferencesSync(userId: String): UserPreferencesEntity?

    @Query("DELETE FROM user_preferences WHERE userId = :userId")
    suspend fun delete(userId: String)
}
