package com.eyezen.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Water intake entity for tracking water consumption.
 *
 * Records each water intake event.
 */
@Entity(tableName = "water_intake")
data class WaterIntakeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String?,
    val date: LocalDate,
    val timestamp: LocalDateTime,
    val amount: Int, // in ml
    val syncedToCloud: Boolean = false,
    val cloudId: String? = null
)
