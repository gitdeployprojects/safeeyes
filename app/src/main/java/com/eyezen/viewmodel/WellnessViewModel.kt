package com.eyezen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyezen.data.model.DailyWellness
import com.eyezen.data.model.Recommendation
import com.eyezen.domain.WellnessAIEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for wellness analytics and AI recommendations.
 *
 * Manages:
 * - Wellness score calculations
 * - Recommendations generation
 * - Reports and analytics
 */
@HiltViewModel
class WellnessViewModel @Inject constructor() : ViewModel() {

    private val aiEngine = WellnessAIEngine()

    private val _dailyWellness = MutableStateFlow<DailyWellness?>(null)
    val dailyWellness: StateFlow<DailyWellness?> = _dailyWellness.asStateFlow()

    private val _recommendations = MutableStateFlow<List<Recommendation>>(emptyList())
    val recommendations: StateFlow<List<Recommendation>> = _recommendations.asStateFlow()

    private val _eyeHealthScore = MutableStateFlow(0)
    val eyeHealthScore: StateFlow<Int> = _eyeHealthScore.asStateFlow()

    private val _productivityScore = MutableStateFlow(0)
    val productivityScore: StateFlow<Int> = _productivityScore.asStateFlow()

    private val _wellnessScore = MutableStateFlow(0)
    val wellnessScore: StateFlow<Int> = _wellnessScore.asStateFlow()

    /**
     * Analyze daily wellness data
     */
    fun analyzeDailyWellness(
        screenTime: Long,
        totalBreaks: Int,
        completedBreaks: Int,
        waterIntake: Int,
        waterGoal: Int = 2000,
        exercisesCompleted: Int = 0
    ) {
        viewModelScope.launch {
            try {
                val wellness = aiEngine.calculateDailyWellness(
                    screenTime = screenTime,
                    totalBreaks = totalBreaks,
                    completedBreaks = completedBreaks,
                    waterIntake = waterIntake,
                    waterGoal = waterGoal,
                    exercisesCompleted = exercisesCompleted
                )

                _dailyWellness.value = wellness
                _eyeHealthScore.value = wellness.eyeHealthScore
                _productivityScore.value = wellness.productivityScore
                _wellnessScore.value = wellness.wellnessScore

                // Generate recommendations
                val recs = aiEngine.generateRecommendations(wellness)
                _recommendations.value = recs

                Timber.d(
                    "Wellness analyzed - Eye: ${wellness.eyeHealthScore}, "
                    + "Productivity: ${wellness.productivityScore}, "
                    + "Wellness: ${wellness.wellnessScore}"
                )
            } catch (e: Exception) {
                Timber.e(e, "Failed to analyze wellness")
            }
        }
    }
}
