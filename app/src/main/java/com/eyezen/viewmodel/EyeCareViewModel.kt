package com.eyezen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyezen.data.local.entity.BreakEntity
import com.eyezen.data.repository.BreakRepository
import com.eyezen.data.repository.UserPreferencesRepository
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
 * ViewModel for eye care breaks.
 *
 * Manages:
 * - Break scheduling
 * - Break execution
 * - Break history
 * - 20-20-20 rule
 */
@HiltViewModel
class EyeCareViewModel @Inject constructor(
    private val breakRepository: BreakRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _breakState = MutableStateFlow<BreakState>(BreakState.Idle)
    val breakState: StateFlow<BreakState> = _breakState.asStateFlow()

    private val _todayBreaksCount = MutableStateFlow(0)
    val todayBreaksCount: StateFlow<Int> = _todayBreaksCount.asStateFlow()

    private val _nextBreakTime = MutableStateFlow<Long>(0L)
    val nextBreakTime: StateFlow<Long> = _nextBreakTime.asStateFlow()

    private var currentUserId: String = ""
    private var breakInterval: Int = 20 // minutes
    private var breakDuration: Int = 20 // seconds

    init {
        loadUserPreferences()
    }

    /**
     * Load user preferences
     */
    private fun loadUserPreferences() {
        viewModelScope.launch {
            // TODO: Get userId from AuthRepository
            currentUserId = "user_${System.currentTimeMillis()}"
            Timber.d("User ID set: $currentUserId")
        }
    }

    /**
     * Start a new break session
     */
    fun startBreak(
        type: String = "short",
        duration: Int = breakDuration,
        exerciseType: String? = null
    ) {
        viewModelScope.launch {
            try {
                _breakState.value = BreakState.Running(
                    duration = duration,
                    elapsed = 0,
                    type = type
                )

                val breakEntity = BreakEntity(
                    userId = currentUserId,
                    type = type,
                    duration = duration,
                    completedAt = LocalDateTime.now(),
                    skipped = false,
                    exerciseType = exerciseType
                )

                breakRepository.insertBreak(breakEntity)
                Timber.d("Break started: $type for ${duration}s")
                loadTodayBreaksCount()
            } catch (e: Exception) {
                Timber.e(e, "Failed to start break")
                _breakState.value = BreakState.Error(e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Complete current break
     */
    fun completeBreak() {
        viewModelScope.launch {
            _breakState.value = BreakState.Completed
            Timber.d("Break completed")
            loadTodayBreaksCount()
            scheduleNextBreak()
        }
    }

    /**
     * Skip current break
     */
    fun skipBreak() {
        viewModelScope.launch {
            try {
                val breakEntity = BreakEntity(
                    userId = currentUserId,
                    type = "skipped",
                    duration = 0,
                    completedAt = LocalDateTime.now(),
                    skipped = true
                )
                breakRepository.insertBreak(breakEntity)
                _breakState.value = BreakState.Idle
                Timber.d("Break skipped")
                scheduleNextBreak()
            } catch (e: Exception) {
                Timber.e(e, "Failed to skip break")
            }
        }
    }

    /**
     * Snooze break for specified minutes
     */
    fun snoozeBreak(minutes: Int = 5) {
        viewModelScope.launch {
            _breakState.value = BreakState.Idle
            _nextBreakTime.value = System.currentTimeMillis() + (minutes * 60 * 1000)
            Timber.d("Break snoozed for $minutes minutes")
        }
    }

    /**
     * Load today's break count
     */
    private fun loadTodayBreaksCount() {
        viewModelScope.launch {
            val count = breakRepository.getCompletedBreaksCount(
                currentUserId,
                LocalDate.now()
            )
            _todayBreaksCount.value = count
            Timber.d("Today's breaks: $count")
        }
    }

    /**
     * Schedule next break
     */
    private fun scheduleNextBreak() {
        _nextBreakTime.value = System.currentTimeMillis() + (breakInterval * 60 * 1000)
        Timber.d("Next break scheduled in $breakInterval minutes")
    }
}

/**
 * Break state sealed class
 */
sealed class BreakState {
    object Idle : BreakState()
    data class Running(
        val duration: Int,
        val elapsed: Int,
        val type: String
    ) : BreakState()
    object Completed : BreakState()
    data class Error(val message: String) : BreakState()
}
