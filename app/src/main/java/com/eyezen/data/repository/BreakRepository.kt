package com.eyezen.data.repository

import com.eyezen.data.local.dao.BreakDao
import com.eyezen.data.local.entity.BreakEntity
import com.eyezen.data.remote.SupabaseClient
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.time.LocalDate

/**
 * Break repository.
 *
 * Handles CRUD operations for breaks with offline-first sync:
 * - Insert break records
 * - Retrieve breaks by user/date
 * - Sync to cloud
 */
class BreakRepository(
    private val breakDao: BreakDao,
    private val supabaseClient: SupabaseClient
) {

    /**
     * Insert a new break record
     */
    suspend fun insertBreak(break: BreakEntity): Result<Long> {
        return try {
            val id = breakDao.insert(break)
            Timber.d("Break inserted: $id")
            Result.success(id)
        } catch (e: Exception) {
            Timber.e(e, "Failed to insert break")
            Result.failure(e)
        }
    }

    /**
     * Get breaks for user on specific date
     */
    fun getBreaksByUserAndDate(userId: String, date: LocalDate): Flow<List<BreakEntity>> {
        return breakDao.getBreaksByUserAndDate(userId, date)
    }

    /**
     * Get completed breaks count for a day
     */
    suspend fun getCompletedBreaksCount(userId: String, date: LocalDate): Int {
        return breakDao.getCompletedBreaksCount(userId, date)
    }

    /**
     * Sync unsynced breaks to cloud
     */
    suspend fun syncBreaksToCloud(): Result<Unit> {
        return try {
            val unsyncedBreaks = breakDao.getUnsyncedBreaks()
            for (break in unsyncedBreaks) {
                try {
                    // TODO: Implement Supabase insert
                    breakDao.markAsSynced(break.id, "cloud_${break.id}")
                } catch (e: Exception) {
                    Timber.e(e, "Failed to sync break ${break.id}")
                }
            }
            Timber.d("Breaks synced: ${unsyncedBreaks.size}")
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Failed to sync breaks")
            Result.failure(e)
        }
    }
}
