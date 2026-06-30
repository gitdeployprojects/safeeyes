package com.eyezen.data.model

import java.time.LocalDate

/**
 * User streak for gamification.
 */
data class Streak(
    val id: String,
    val userId: String,
    val type: String, // "breaks", "water", "exercises", "overall"
    val currentStreak: Int,
    val longestStreak: Int,
    val lastCompletedDate: LocalDate,
    val startDate: LocalDate
)

/**
 * User badge/achievement.
 */
data class Badge(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val category: String, // "breaks", "water", "exercises", "wellness", "milestone"
    val requiredValue: Int,
    val unlockedDate: LocalDate? = null,
    val isUnlocked: Boolean = false
)

/**
 * User experience and level.
 */
data class UserXP(
    val userId: String,
    val totalXP: Int,
    val level: Int,
    val xpToNextLevel: Int,
    val currentLevelXP: Int
)

/**
 * Gamification achievement.
 */
data class Achievement(
    val id: String,
    val name: String,
    val description: String,
    val xpReward: Int,
    val type: String, // "break", "water", "exercise", "milestone"
    val requirement: String,
    val unlockedDate: LocalDate? = null
)

/**
 * Badge registry with predefined badges.
 */
object BadgeRegistry {
    val badges = listOf(
        // Break badges
        Badge(
            id = "badge_break_1",
            name = "First Break",
            description = "Complete your first break",
            icon = "🎉",
            category = "breaks",
            requiredValue = 1
        ),
        Badge(
            id = "badge_break_5",
            name = "Break Master",
            description = "Complete 5 breaks in a day",
            icon = "🏆",
            category = "breaks",
            requiredValue = 5
        ),
        Badge(
            id = "badge_break_10",
            name = "Break Legend",
            description = "Complete 10 breaks in a day",
            icon = "👑",
            category = "breaks",
            requiredValue = 10
        ),

        // Water badges
        Badge(
            id = "badge_water_1",
            name = "Hydration Start",
            description = "Drink your first glass of water",
            icon = "💧",
            category = "water",
            requiredValue = 1
        ),
        Badge(
            id = "badge_water_8",
            name = "Hydration Goal",
            description = "Reach daily water goal (8 glasses)",
            icon = "💦",
            category = "water",
            requiredValue = 8
        ),
        Badge(
            id = "badge_water_7day",
            name = "Hydration Warrior",
            description = "7-day water streak",
            icon = "🌊",
            category = "water",
            requiredValue = 7
        ),

        // Exercise badges
        Badge(
            id = "badge_exercise_1",
            name = "First Stretch",
            description = "Complete your first exercise",
            icon = "🧘",
            category = "exercises",
            requiredValue = 1
        ),
        Badge(
            id = "badge_exercise_5",
            name = "Exercise Enthusiast",
            description = "Complete 5 exercises",
            icon = "💪",
            category = "exercises",
            requiredValue = 5
        ),

        // Wellness badges
        Badge(
            id = "badge_wellness_80",
            name = "Wellness Champion",
            description = "Reach 80+ wellness score",
            icon = "✨",
            category = "wellness",
            requiredValue = 80
        ),
        Badge(
            id = "badge_wellness_100",
            name = "Perfect Day",
            description = "Achieve 100 wellness score",
            icon = "🌟",
            category = "wellness",
            requiredValue = 100
        ),

        // Milestone badges
        Badge(
            id = "badge_milestone_week",
            name = "Weekly Warrior",
            description = "7-day consistency streak",
            icon = "📅",
            category = "milestone",
            requiredValue = 7
        ),
        Badge(
            id = "badge_milestone_month",
            name = "Monthly Master",
            description = "30-day consistency streak",
            icon = "🎊",
            category = "milestone",
            requiredValue = 30
        )
    )

    fun getBadgeById(id: String): Badge? {
        return badges.find { it.id == id }
    }

    fun getBadgesByCategory(category: String): List<Badge> {
        return badges.filter { it.category == category }
    }
}
