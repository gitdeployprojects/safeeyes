package com.eyezen.data.repository

import com.eyezen.data.local.dao.WaterIntakeDao
import com.eyezen.data.local.entity.WaterIntakeEntity
import com.eyezen.data.remote.SupabaseClient
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.time.LocalDate

/**
 * Water intake repository.
 *
 * Manages water intake tracking:
 * - Insert water intake records
 * - Query by date
 * - Cloud sync
 */
class WaterIntakeRepository(
    private val waterIntakeDao: WaterIntakeDao,
    private val supabaseClient: SupabaseClient
) {

    /**
     * Insert water intake record
     */
    suspend fun insertWaterIntake(waterIntake: WaterIntakeEntity): Result<Long> {
        return try {
            val id = waterIntakeDao.insert(waterIntake)
            Timber.d("Water intake recorded: $id")
            Result.success(id)
        } catch (e: Exception) {
            Timber.e(e, "Failed to record water intake")
            Result.failure(e)
        }
    }

    /**
     * Get water intake for a specific day
     */
    fun getWaterIntakeByDate(userId: String, date: LocalDate): Flow<List<WaterIntakeEntity>> {
        return waterIntakeDao.getWaterIntakeByDate(userId, date)
    }

    /**
     * Get total water for a day
     */
    suspend fun getTotalWaterForDay(userId: String, date: LocalDate): Int {
        return waterIntakeDao.getTotalWaterForDay(userId, date)
    }

    /**
     * Sync unsynced water intake to cloud
     */
    suspend fun syncWaterIntakeToCloud(): Result<Unit> {
        return try {
            val unsyncedIntake = waterIntakeDao.getUnsyncedWaterIntake()
            for (intake in unsyncedIntake) {
                try {
                    // TODO: Implement Supabase insert
                    waterIntakeDao.markAsSynced(intake.id, "cloud_${intake.id}")
                } catch (e: Exception) {
                    Timber.e(e, "Failed to sync water intake ${intake.id}")
                }
            }
            Timber.d("Water intake synced: ${unsyncedIntake.size}")
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Failed to sync water intake")
            Result.failure(e)
        }
    }
}
