package com.eyezen.data.model

import java.time.LocalDate

/**
 * Daily wellness snapshot.
 */
data class DailyWellness(
    val date: LocalDate,
    val totalScreenTime: Long, // milliseconds
    val totalBreaks: Int,
    val completedBreaks: Int,
    val skippedBreaks: Int,
    val totalWaterIntake: Int, // ml
    val waterGoal: Int, // ml
    val exercisesCompleted: Int,
    val productivityScore: Int, // 0-100
    val eyeHealthScore: Int, // 0-100
    val wellnessScore: Int // 0-100
)

/**
 * Recommendation data model.
 */
data class Recommendation(
    val id: String,
    val title: String,
    val description: String,
    val category: String, // "break", "water", "exercise", "habit"
    val priority: String, // "high", "medium", "low"
    val action: String? = null,
    val reason: String
)

/**
 * Weekly report.
 */
data class WeeklyReport(
    val startDate: LocalDate,
    val endDate: LocalDate,
    val totalScreenTime: Long,
    val totalBreaks: Int,
    val totalWaterIntake: Int,
    val averageEyeHealthScore: Int,
    val averageProductivityScore: Int,
    val averageWellnessScore: Int,
    val bestDay: String,
    val worseDay: String,
    val recommendations: List<Recommendation>
)
