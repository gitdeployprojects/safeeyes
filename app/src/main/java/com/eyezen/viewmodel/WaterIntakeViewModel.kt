package com.eyezen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyezen.data.local.entity.WaterIntakeEntity
import com.eyezen.data.repository.WaterIntakeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * ViewModel for water intake tracking.
 *
 * Manages:
 * - Water intake recording
 * - Daily water goal progress
 * - Water intake history
 * - Statistics
 */
@HiltViewModel
class WaterIntakeViewModel @Inject constructor(
    private val waterIntakeRepository: WaterIntakeRepository
) : ViewModel() {

    private val _totalWaterToday = MutableStateFlow(0)
    val totalWaterToday: StateFlow<Int> = _totalWaterToday.asStateFlow()

    private val _waterHistory = MutableStateFlow<List<WaterIntakeEntity>>(emptyList())
    val waterHistory: StateFlow<List<WaterIntakeEntity>> = _waterHistory.asStateFlow()

    private val _dailyGoal = MutableStateFlow(8) // glasses
    val dailyGoal: StateFlow<Int> = _dailyGoal.asStateFlow()

    private val _progressPercentage = MutableStateFlow(0f)
    val progressPercentage: StateFlow<Float> = _progressPercentage.asStateFlow()

    private var currentUserId: String = ""
    private val glassVolume = 250 // ml

    init {
        initializeUser()
    }

    /**
     * Initialize user
     */
    private fun initializeUser() {
        viewModelScope.launch {
            // TODO: Get userId from AuthRepository
            currentUserId = "user_${System.currentTimeMillis()}"
            loadTodayWaterIntake()
            Timber.d("Water intake ViewModel initialized for $currentUserId")
        }
    }

    /**
     * Add water intake record
     */
    fun addWaterIntake(amountMl: Int = 250) {
        viewModelScope.launch {
            try {
                val waterIntake = WaterIntakeEntity(
                    userId = currentUserId,
                    date = LocalDate.now(),
                    timestamp = LocalDateTime.now(),
                    amount = amountMl
                )

                waterIntakeRepository.insertWaterIntake(waterIntake)
                Timber.d("Water intake recorded: ${amountMl}ml")
                loadTodayWaterIntake()
            } catch (e: Exception) {
                Timber.e(e, "Failed to record water intake")
            }
        }
    }

    /**
     * Add quick water (250ml glass)
     */
    fun addQuickWater() {
        addWaterIntake(glassVolume)
    }

    /**
     * Load today's water intake
     */
    private fun loadTodayWaterIntake() {
        viewModelScope.launch {
            try {
                val totalMl = waterIntakeRepository.getTotalWaterForDay(
                    currentUserId,
                    LocalDate.now()
                )
                val glasses = totalMl / glassVolume
                _totalWaterToday.value = glasses

                // Calculate progress
                val goal = _dailyGoal.value
                val progress = (glasses.toFloat() / goal).coerceIn(0f, 1f)
                _progressPercentage.value = progress

                Timber.d("Today's water: $glasses glasses ($totalMl ml)")
            } catch (e: Exception) {
                Timber.e(e, "Failed to load water intake")
            }
        }
    }

    /**
     * Set daily water goal
     */
    fun setDailyGoal(glasses: Int) {
        _dailyGoal.value = glasses
        viewModelScope.launch {
            loadTodayWaterIntake()
        }
    }
}
