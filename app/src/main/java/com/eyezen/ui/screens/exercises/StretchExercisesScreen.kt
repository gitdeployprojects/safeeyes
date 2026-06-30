package com.eyezen.ui.screens.exercises

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eyezen.data.model.Exercise
import com.eyezen.viewmodel.StretchExerciseViewModel

/**
 * Stretch exercises library screen.
 *
 * Displays:
 * - Exercises by category
 * - Exercise details
 * - Start exercise button
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StretchExercisesScreen(
    stretchExerciseViewModel: StretchExerciseViewModel = hiltViewModel(),
    onStartExercise: (Exercise) -> Unit = {}
) {
    val exercises by stretchExerciseViewModel.exercises.collectAsState()
    val categories by stretchExerciseViewModel.categories.collectAsState()
    var selectedCategory by remember { mutableStateOf(categories.firstOrNull() ?: "eye") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stretch Exercises") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Category tabs
            ScrollableTabRow(
                selectedTabIndex = categories.indexOf(selectedCategory)
            ) {
                categories.forEach { category ->
                    Tab(
                        selected = selectedCategory == category,
                        onClick = {
                            selectedCategory = category
                            stretchExerciseViewModel.filterByCategory(category)
                        },
                        text = { Text(getCategoryLabel(category)) }
                    )
                }
            }

            // Exercises list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(exercises) { exercise ->
                    ExerciseCard(
                        exercise = exercise,
                        onSelect = {
                            stretchExerciseViewModel.selectExercise(exercise)
                            onStartExercise(exercise)
                        }
                    )
                }
            }
        }
    }
}

/**
 * Exercise card component.
 */
@Composable
private fun ExerciseCard(
    exercise: Exercise,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = exercise.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${exercise.duration}s",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Get category display label
 */
private fun getCategoryLabel(category: String): String {
    return when (category) {
        "eye" -> "Eyes"
        "neck" -> "Neck"
        "shoulder" -> "Shoulders"
        "wrist" -> "Wrists"
        "back" -> "Back"
        "breathing" -> "Breathing"
        else -> category
    }
}
