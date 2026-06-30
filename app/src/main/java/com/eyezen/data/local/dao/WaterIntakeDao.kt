package com.eyezen.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.eyezen.data.local.entity.WaterIntakeEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Data Access Object for WaterIntake entities.
 */
@Dao
interface WaterIntakeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(waterIntake: WaterIntakeEntity): Long

    @Update
    suspend fun update(waterIntake: WaterIntakeEntity)

    @Delete
    suspend fun delete(waterIntake: WaterIntakeEntity)

    @Query("SELECT * FROM water_intake WHERE userId = :userId AND date = :date ORDER BY timestamp DESC")
    fun getWaterIntakeByDate(userId: String, date: LocalDate): Flow<List<WaterIntakeEntity>>

    @Query("SELECT SUM(amount) FROM water_intake WHERE userId = :userId AND date = :date")
    suspend fun getTotalWaterForDay(userId: String, date: LocalDate): Int

    @Query("SELECT * FROM water_intake WHERE syncedToCloud = 0")
    suspend fun getUnsyncedWaterIntake(): List<WaterIntakeEntity>

    @Query("UPDATE water_intake SET syncedToCloud = 1, cloudId = :cloudId WHERE id = :id")
    suspend fun markAsSynced(id: Long, cloudId: String)

    @Query("DELETE FROM water_intake WHERE userId = :userId")
    suspend fun deleteAllByUser(userId: String)
}
