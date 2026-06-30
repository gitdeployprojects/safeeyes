package com.eyezen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyezen.data.model.Badge
import com.eyezen.data.model.BadgeRegistry
import com.eyezen.data.model.Streak
import com.eyezen.data.model.UserXP
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

/**
 * ViewModel for gamification features.
 *
 * Manages:
 * - User streaks
 * - XP and levels
 * - Badges and achievements
 * - Leaderboards
 */
@HiltViewModel
class GamificationViewModel @Inject constructor() : ViewModel() {

    private val _userXP = MutableStateFlow<UserXP?>(null)
    val userXP: StateFlow<UserXP?> = _userXP.asStateFlow()

    private val _streaks = MutableStateFlow<List<Streak>>(emptyList())
    val streaks: StateFlow<List<Streak>> = _streaks.asStateFlow()

    private val _unlockedBadges = MutableStateFlow<List<Badge>>(emptyList())
    val unlockedBadges: StateFlow<List<Badge>> = _unlockedBadges.asStateFlow()

    private val _allBadges = MutableStateFlow<List<Badge>>(BadgeRegistry.badges)
    val allBadges: StateFlow<List<Badge>> = _allBadges.asStateFlow()

    private val _recentAchievements = MutableStateFlow<List<String>>(emptyList())
    val recentAchievements: StateFlow<List<String>> = _recentAchievements.asStateFlow()

    private var currentUserId: String = ""

    init {
        initializeUser()
    }

    /**
     * Initialize user
     */
    private fun initializeUser() {
        viewModelScope.launch {
            currentUserId = "user_${System.currentTimeMillis()}"
            initializeUserXP()
            initializeStreaks()
            Timber.d("Gamification ViewModel initialized")
        }
    }

    /**
     * Initialize user XP
     */
    private fun initializeUserXP() {
        _userXP.value = UserXP(
            userId = currentUserId,
            totalXP = 0,
            level = 1,
            xpToNextLevel = 100,
            currentLevelXP = 0
        )
    }

    /**
     * Initialize user streaks
     */
    private fun initializeStreaks() {
        _streaks.value = listOf(
            Streak(
                id = "streak_breaks",
                userId = currentUserId,
                type = "breaks",
                currentStreak = 0,
                longestStreak = 0,
                lastCompletedDate = LocalDate.now(),
                startDate = LocalDate.now()
            ),
            Streak(
                id = "streak_water",
                userId = currentUserId,
                type = "water",
                currentStreak = 0,
                longestStreak = 0,
                lastCompletedDate = LocalDate.now(),
                startDate = LocalDate.now()
            ),
            Streak(
                id = "streak_exercises",
                userId = currentUserId,
                type = "exercises",
                currentStreak = 0,
                longestStreak = 0,
                lastCompletedDate = LocalDate.now(),
                startDate = LocalDate.now()
            )
        )
    }

    /**
     * Add XP and check for level up
     */
    fun addXP(amount: Int) {
        viewModelScope.launch {
            val current = _userXP.value ?: return@launch
            val newTotalXP = current.totalXP + amount
            val newCurrentLevelXP = current.currentLevelXP + amount

            var newLevel = current.level
            var newXPToNextLevel = current.xpToNextLevel
            var leveledUpXP = newCurrentLevelXP

            // Check for level up
            while (leveledUpXP >= current.xpToNextLevel) {
                leveledUpXP -= current.xpToNextLevel
                newLevel++
                newXPToNextLevel = 100 * newLevel // XP requirement increases per level
            }

            _userXP.value = UserXP(
                userId = current.userId,
                totalXP = newTotalXP,
                level = newLevel,
                xpToNextLevel = newXPToNextLevel,
                currentLevelXP = leveledUpXP
            )

            if (newLevel > current.level) {
                Timber.d("Level up! New level: $newLevel")
            }
        }
    }

    /**
     * Update break streak
     */
    fun updateBreakStreak() {
        updateStreak("breaks")
        addXP(10)
    }

    /**
     * Update water streak
     */
    fun updateWaterStreak() {
        updateStreak("water")
        addXP(5)
    }

    /**
     * Update exercise streak
     */
    fun updateExerciseStreak() {
        updateStreak("exercises")
        addXP(15)
    }

    /**
     * Update streak
     */
    private fun updateStreak(type: String) {
        viewModelScope.launch {
            val currentStreaks = _streaks.value.toMutableList()
            val streakIndex = currentStreaks.indexOfFirst { it.type == type }

            if (streakIndex != -1) {
                val streak = currentStreaks[streakIndex]
                val today = LocalDate.now()

                val newStreak = if (streak.lastCompletedDate == today) {
                    // Already completed today
                    streak
                } else if (streak.lastCompletedDate.plusDays(1) == today) {
                    // Continuing streak
                    val newCurrent = streak.currentStreak + 1
                    streak.copy(
                        currentStreak = newCurrent,
                        longestStreak = maxOf(newCurrent, streak.longestStreak),
                        lastCompletedDate = today
                    )
                } else {
                    // Streak broken, start new
                    streak.copy(
                        currentStreak = 1,
                        lastCompletedDate = today
                    )
                }

                currentStreaks[streakIndex] = newStreak
                _streaks.value = currentStreaks
                Timber.d("$type streak updated: ${newStreak.currentStreak}")
            }
        }
    }

    /**
     * Unlock badge
     */
    fun unlockBadge(badgeId: String) {
        viewModelScope.launch {
            val badge = BadgeRegistry.getBadgeById(badgeId) ?: return@launch

            val unlockedBadge = badge.copy(
                unlockedDate = LocalDate.now(),
                isUnlocked = true
            )

            val currentUnlocked = _unlockedBadges.value.toMutableList()
            if (!currentUnlocked.any { it.id == badgeId }) {
                currentUnlocked.add(unlockedBadge)
                _unlockedBadges.value = currentUnlocked

                // Add to recent achievements
                val recent = _recentAchievements.value.toMutableList()
                recent.add(0, badge.name)
                if (recent.size > 5) recent.removeAt(5)
                _recentAchievements.value = recent

                Timber.d("Badge unlocked: ${badge.name}")
                addXP(20) // Bonus XP for unlocking badge
            }
        }
    }

    /**
     * Get current level progress percentage
     */
    fun getLevelProgress(): Float {
        val xp = _userXP.value ?: return 0f
        return (xp.currentLevelXP.toFloat() / xp.xpToNextLevel).coerceIn(0f, 1f)
    }
}
