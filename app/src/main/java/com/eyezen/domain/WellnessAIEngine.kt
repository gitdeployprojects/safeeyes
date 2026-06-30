package com.eyezen.domain

import com.eyezen.data.model.DailyWellness
import com.eyezen.data.model.Recommendation
import com.eyezen.data.model.WeeklyReport
import timber.log.Timber
import java.time.LocalDate
import kotlin.math.min

/**
 * AI Engine for local wellness analysis and recommendations.
 *
 * Analyzes user behavior and generates:
 * - Health scores
 * - Productivity scores
 * - Wellness scores
 * - Personalized recommendations
 */
class WellnessAIEngine {

    /**
     * Calculate daily wellness metrics
     */
    fun calculateDailyWellness(
        screenTime: Long,
        totalBreaks: Int,
        completedBreaks: Int,
        waterIntake: Int,
        waterGoal: Int,
        exercisesCompleted: Int
    ): DailyWellness {
        val eyeHealthScore = calculateEyeHealthScore(
            screenTime,
            completedBreaks,
            totalBreaks
        )

        val productivityScore = calculateProductivityScore(
            screenTime,
            completedBreaks,
            exercisesCompleted
        )

        val wellnessScore = calculateWellnessScore(
            eyeHealthScore,
            productivityScore,
            waterIntake,
            waterGoal
        )

        return DailyWellness(
            date = LocalDate.now(),
            totalScreenTime = screenTime,
            totalBreaks = totalBreaks,
            completedBreaks = completedBreaks,
            skippedBreaks = totalBreaks - completedBreaks,
            totalWaterIntake = waterIntake,
            waterGoal = waterGoal,
            exercisesCompleted = exercisesCompleted,
            productivityScore = productivityScore,
            eyeHealthScore = eyeHealthScore,
            wellnessScore = wellnessScore
        )
    }

    /**
     * Calculate eye health score (0-100)
     *
     * Factors:
     * - Screen time (lower is better)
     * - Break compliance
     * - Exercise completion
     */
    private fun calculateEyeHealthScore(
        screenTime: Long,
        completedBreaks: Int,
        totalBreaks: Int
    ): Int {
        val screenTimeMinutes = screenTime / (1000 * 60)

        // Base score from screen time
        val screenTimeScore = when {
            screenTimeMinutes <= 240 -> 100 // 4 hours or less
            screenTimeMinutes <= 480 -> 80 // 8 hours
            screenTimeMinutes <= 600 -> 60 // 10 hours
            screenTimeMinutes <= 720 -> 40 // 12 hours
            else -> 20 // More than 12 hours
        }

        // Break compliance bonus
        val breakCompliance = if (totalBreaks > 0) {
            (completedBreaks.toFloat() / totalBreaks) * 100
        } else {
            0f
        }

        // Weighted calculation: 70% screen time, 30% break compliance
        val score = (screenTimeScore * 0.7f + breakCompliance * 0.3f).toInt()
        return score.coerceIn(0, 100)
    }

    /**
     * Calculate productivity score (0-100)
     *
     * Factors:
     * - Screen time patterns
     * - Break frequency
     * - Exercise completion
     */
    private fun calculateProductivityScore(
        screenTime: Long,
        completedBreaks: Int,
        exercisesCompleted: Int
    ): Int {
        val screenTimeMinutes = screenTime / (1000 * 60)

        // Optimal screen time with breaks is more productive
        val screenTimeScore = when {
            screenTimeMinutes <= 480 -> 85 // 8 hours good for productivity
            screenTimeMinutes <= 600 -> 70
            screenTimeMinutes <= 720 -> 50
            else -> 30
        }

        // Break benefits productivity
        val breakBonus = min(completedBreaks * 5, 15)

        // Exercise boosts productivity
        val exerciseBonus = min(exercisesCompleted * 3, 10)

        val score = screenTimeScore + breakBonus + exerciseBonus
        return score.coerceIn(0, 100)
    }

    /**
     * Calculate overall wellness score (0-100)
     *
     * Factors:
     * - Eye health
     * - Productivity
     * - Hydration
     */
    private fun calculateWellnessScore(
        eyeHealthScore: Int,
        productivityScore: Int,
        waterIntake: Int,
        waterGoal: Int
    ): Int {
        // Hydration score
        val hydrationRatio = (waterIntake.toFloat() / waterGoal).coerceIn(0f, 1f)
        val hydrationScore = (hydrationRatio * 100).toInt()

        // Weighted average: 40% eye health, 35% productivity, 25% hydration
        val score = (
            eyeHealthScore * 0.4f +
            productivityScore * 0.35f +
            hydrationScore * 0.25f
        ).toInt()

        return score.coerceIn(0, 100)
    }

    /**
     * Generate personalized recommendations
     */
    fun generateRecommendations(
        wellness: DailyWellness
    ): List<Recommendation> {
        val recommendations = mutableListOf<Recommendation>()

        // Eye health recommendations
        if (wellness.eyeHealthScore < 50) {
            recommendations.add(
                Recommendation(
                    id = "rec_eye_1",
                    title = "Increase Break Frequency",
                    description = "Your eye health score is low. Try taking breaks more frequently.",
                    category = "break",
                    priority = "high",
                    reason = "Eye strain detected from extended screen time"
                )
            )
        }

        // Screen time recommendations
        if (wellness.totalScreenTime > 600 * 60 * 1000) { // More than 10 hours
            recommendations.add(
                Recommendation(
                    id = "rec_screen_1",
                    title = "Reduce Screen Time",
                    description = "You've exceeded recommended screen time. Consider taking a longer break.",
                    category = "break",
                    priority = "high",
                    reason = "Extended screen exposure"
                )
            )
        }

        // Water intake recommendations
        if (wellness.totalWaterIntake < wellness.waterGoal / 2) {
            recommendations.add(
                Recommendation(
                    id = "rec_water_1",
                    title = "Stay Hydrated",
                    description = "You're not drinking enough water. Aim for your daily goal.",
                    category = "water",
                    priority = "medium",
                    reason = "Low water intake"
                )
            )
        }

        // Exercise recommendations
        if (wellness.exercisesCompleted == 0) {
            recommendations.add(
                Recommendation(
                    id = "rec_exercise_1",
                    title = "Do Stretch Exercises",
                    description = "You haven't done any stretches today. Try a quick exercise routine.",
                    category = "exercise",
                    priority = "medium",
                    reason = "No physical activity"
                )
            )
        }

        // Positive habit recommendations
        if (wellness.completedBreaks >= wellness.totalBreaks / 2) {
            recommendations.add(
                Recommendation(
                    id = "rec_habit_1",
                    title = "Great Break Compliance!",
                    description = "You're doing well with your breaks. Keep it up!",
                    category = "habit",
                    priority = "low",
                    reason = "Good break adherence"
                )
            )
        }

        Timber.d("Generated ${recommendations.size} recommendations")
        return recommendations
    }

    /**
     * Generate weekly report
     */
    fun generateWeeklyReport(
        dailyWellnessData: List<DailyWellness>
    ): WeeklyReport? {
        if (dailyWellnessData.isEmpty()) return null

        val startDate = dailyWellnessData.minOf { it.date }
        val endDate = dailyWellnessData.maxOf { it.date }

        val totalScreenTime = dailyWellnessData.sumOf { it.totalScreenTime }
        val totalBreaks = dailyWellnessData.sumOf { it.completedBreaks }
        val totalWater = dailyWellnessData.sumOf { it.totalWaterIntake }

        val avgEyeHealth = dailyWellnessData.map { it.eyeHealthScore }.average().toInt()
        val avgProductivity = dailyWellnessData.map { it.productivityScore }.average().toInt()
        val avgWellness = dailyWellnessData.map { it.wellnessScore }.average().toInt()

        val bestDay = dailyWellnessData.maxByOrNull { it.wellnessScore }?.date?.toString() ?: ""
        val worseDay = dailyWellnessData.minByOrNull { it.wellnessScore }?.date?.toString() ?: ""

        val recommendations = dailyWellnessData.lastOrNull()?.let {
            generateRecommendations(it)
        } ?: emptyList()

        return WeeklyReport(
            startDate = startDate,
            endDate = endDate,
            totalScreenTime = totalScreenTime,
            totalBreaks = totalBreaks,
            totalWaterIntake = totalWater,
            averageEyeHealthScore = avgEyeHealth,
            averageProductivityScore = avgProductivity,
            averageWellnessScore = avgWellness,
            bestDay = bestDay,
            worseDay = worseDay,
            recommendations = recommendations
        )
    }
}
