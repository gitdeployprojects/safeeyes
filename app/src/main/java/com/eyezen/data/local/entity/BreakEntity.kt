package com.eyezen.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * Break entity for eye care sessions.
 *
 * Represents a single break session (20-20-20 rule or custom break).
 */
@Entity(tableName = "breaks")
data class BreakEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String?,
    val type: String, // "short", "long", "custom"
    val duration: Int, // in seconds
    val completedAt: LocalDateTime,
    val skipped: Boolean = false,
    val exerciseType: String? = null, // "eye", "neck", "shoulder", etc.
    val syncedToCloud: Boolean = false,
    val cloudId: String? = null
)
