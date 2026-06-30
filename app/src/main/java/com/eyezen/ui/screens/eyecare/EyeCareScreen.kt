package com.eyezen.ui.screens.eyecare

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eyezen.viewmodel.EyeCareViewModel

/**
 * Eye care screen.
 *
 * Displays:
 * - 20-20-20 rule explanation
 * - Break options (short, long)
 * - Today's break statistics
 * - Exercise types
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EyeCareScreen(
    eyeCareViewModel: EyeCareViewModel = hiltViewModel(),
    onStartBreak: () -> Unit = {}
) {
    val todayBreaksCount by eyeCareViewModel.todayBreaksCount.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Eye Care") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 20-20-20 Rule Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "20-20-20 Rule",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Every 20 minutes, look at something 20 feet away for 20 seconds.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "This reduces eye strain and prevents digital eye fatigue.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Today's Stats
            Text(
                text = "Today's Breaks: $todayBreaksCount",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Short Break Button
            Button(
                onClick = {
                    eyeCareViewModel.startBreak(
                        type = "short",
                        duration = 20,
                        exerciseType = null
                    )
                    onStartBreak()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Text("Start Short Break (20s)")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Long Break Button
            Button(
                onClick = {
                    eyeCareViewModel.startBreak(
                        type = "long",
                        duration = 300,
                        exerciseType = null
                    )
                    onStartBreak()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Text("Start Long Break (5m)")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Exercise Types
            Text(
                text = "Stretch Exercises",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExerciseOption(
                title = "Eye Exercises",
                description = "Roll eyes, blink, focus shifts",
                onSelect = {
                    eyeCareViewModel.startBreak(
                        type = "long",
                        duration = 120,
                        exerciseType = "eye"
                    )
                    onStartBreak()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExerciseOption(
                title = "Neck Stretch",
                description = "Gentle neck rotations and stretches",
                onSelect = {
                    eyeCareViewModel.startBreak(
                        type = "long",
                        duration = 120,
                        exerciseType = "neck"
                    )
                    onStartBreak()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            ExerciseOption(
                title = "Shoulder Stretch",
                description = "Roll shoulders, release tension",
                onSelect = {
                    eyeCareViewModel.startBreak(
                        type = "long",
                        duration = 120,
                        exerciseType = "shoulder"
                    )
                    onStartBreak()
                }
            )
        }
    }
}

/**
 * Exercise option card component.
 */
@Composable
private fun ExerciseOption(
    title: String,
    description: String,
    onSelect: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onSelect,
                modifier = Modifier
                    .align(Alignment.End)
            ) {
                Text("Start")
            }
        }
    }
}
