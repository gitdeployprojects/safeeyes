package com.eyezen.ui.screens.water

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eyezen.viewmodel.WaterIntakeViewModel
import com.eyezen.ui.components.LabeledProgressBar

/**
 * Water intake tracking screen.
 *
 * Displays:
 * - Daily water goal progress
 * - Water intake history
 * - Quick add buttons
 * - Statistics
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterTrackerScreen(
    waterIntakeViewModel: WaterIntakeViewModel = hiltViewModel()
) {
    val totalWaterToday by waterIntakeViewModel.totalWaterToday.collectAsState()
    val dailyGoal by waterIntakeViewModel.dailyGoal.collectAsState()
    val progressPercentage by waterIntakeViewModel.progressPercentage.collectAsState()
    val waterHistory by waterIntakeViewModel.waterHistory.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Water Intake") }
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
            // Daily Goal Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Today's Goal",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "$totalWaterToday / $dailyGoal glasses",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Progress bar
                    LabeledProgressBar(
                        label = "Daily Progress",
                        progress = progressPercentage,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Add Buttons
            Text(
                text = "Quick Add",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickAddButton(
                    label = "250ml",
                    onClick = { waterIntakeViewModel.addWaterIntake(250) }
                )
                QuickAddButton(
                    label = "500ml",
                    onClick = { waterIntakeViewModel.addWaterIntake(500) }
                )
                QuickAddButton(
                    label = "750ml",
                    onClick = { waterIntakeViewModel.addWaterIntake(750) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Large Add Button
            Button(
                onClick = { waterIntakeViewModel.addQuickWater() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text("Add 1 Glass (250ml)")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Hydration Tips
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Hydration Tips",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Drink water throughout the day")
                    Text("• Drink more during breaks")
                    Text("• Keep a water bottle nearby")
                    Text("• Proper hydration improves focus")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Water Intake History
            if (waterHistory.isNotEmpty()) {
                Text(
                    text = "Today's Intake",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                waterHistory.forEach { intake ->
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${intake.amount}ml",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = intake.timestamp.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Quick add button component.
 */
@Composable
private fun QuickAddButton(
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = onClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
