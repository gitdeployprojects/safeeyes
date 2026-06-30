package com.eyezen.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.eyezen.data.local.entity.BreakEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/**
 * Data Access Object for Break entities.
 */
@Dao
interface BreakDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(break: BreakEntity): Long

    @Update
    suspend fun update(break: BreakEntity)

    @Delete
    suspend fun delete(break: BreakEntity)

    @Query("SELECT * FROM breaks WHERE id = :id")
    suspend fun getBreakById(id: Long): BreakEntity?

    @Query("SELECT * FROM breaks WHERE userId = :userId ORDER BY completedAt DESC")
    fun getBreaksByUser(userId: String): Flow<List<BreakEntity>>

    @Query("SELECT * FROM breaks WHERE userId = :userId AND DATE(completedAt) = DATE(:date) ORDER BY completedAt DESC")
    fun getBreaksByUserAndDate(userId: String, date: LocalDate): Flow<List<BreakEntity>>

    @Query("SELECT COUNT(*) FROM breaks WHERE userId = :userId AND DATE(completedAt) = DATE(:date) AND skipped = 0")
    suspend fun getCompletedBreaksCount(userId: String, date: LocalDate): Int

    @Query("SELECT * FROM breaks WHERE syncedToCloud = 0")
    suspend fun getUnsyncedBreaks(): List<BreakEntity>

    @Query("UPDATE breaks SET syncedToCloud = 1, cloudId = :cloudId WHERE id = :id")
    suspend fun markAsSynced(id: Long, cloudId: String)

    @Query("DELETE FROM breaks WHERE userId = :userId")
    suspend fun deleteAllByUser(userId: String)
}
