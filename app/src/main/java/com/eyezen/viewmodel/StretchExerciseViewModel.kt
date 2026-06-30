package com.eyezen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyezen.data.model.Exercise
import com.eyezen.data.model.ExerciseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for stretch exercises.
 *
 * Manages:
 * - Exercise selection
 * - Exercise filtering by category
 * - Exercise progress tracking
 */
@HiltViewModel
class StretchExerciseViewModel @Inject constructor() : ViewModel() {

    private val _exercises = MutableStateFlow<List<Exercise>>(ExerciseDatabase.exercises)
    val exercises: StateFlow<List<Exercise>> = _exercises.asStateFlow()

    private val _selectedExercise = MutableStateFlow<Exercise?>(null)
    val selectedExercise: StateFlow<Exercise?> = _selectedExercise.asStateFlow()

    private val _currentStep = MutableStateFlow(0)
    val currentStep: StateFlow<Int> = _currentStep.asStateFlow()

    private val _categories = MutableStateFlow(
        listOf("eye", "neck", "shoulder", "wrist", "back", "breathing")
    )
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    /**
     * Filter exercises by category
     */
    fun filterByCategory(category: String) {
        val filtered = ExerciseDatabase.getExercisesByCategory(category)
        _exercises.value = filtered
        Timber.d("Filtered exercises for category: $category")
    }

    /**
     * Select an exercise
     */
    fun selectExercise(exercise: Exercise) {
        _selectedExercise.value = exercise
        _currentStep.value = 0
        Timber.d("Selected exercise: ${exercise.name}")
    }

    /**
     * Move to next instruction step
     */
    fun nextStep() {
        val exercise = _selectedExercise.value ?: return
        val nextStep = (_currentStep.value + 1).coerceAtMost(exercise.instructions.size - 1)
        _currentStep.value = nextStep
    }

    /**
     * Move to previous instruction step
     */
    fun previousStep() {
        val previousStep = (_currentStep.value - 1).coerceAtLeast(0)
        _currentStep.value = previousStep
    }

    /**
     * Reset exercise
     */
    fun resetExercise() {
        _currentStep.value = 0
        Timber.d("Exercise reset")
    }
}
