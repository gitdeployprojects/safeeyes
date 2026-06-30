package com.eyezen.ui.screens.analytics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.eyezen.ui.components.ScoreBadge
import com.eyezen.viewmodel.WellnessViewModel

/**
 * Analytics and wellness dashboard.
 *
 * Displays:
 * - Daily wellness scores
 * - Health metrics
 * - AI recommendations
 * - Progress charts
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    wellnessViewModel: WellnessViewModel = hiltViewModel()
) {
    val eyeHealthScore by wellnessViewModel.eyeHealthScore.collectAsState()
    val productivityScore by wellnessViewModel.productivityScore.collectAsState()
    val wellnessScore by wellnessViewModel.wellnessScore.collectAsState()
    val recommendations by wellnessViewModel.recommendations.collectAsState()
    val dailyWellness by wellnessViewModel.dailyWellness.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wellness Analytics") }
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
            // Overall Wellness Score
            Text(
                text = "Today's Wellness",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))

            ScoreBadge(
                score = wellnessScore,
                label = "Wellness Score"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Score breakdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ScoreCard(
                    label = "Eye Health",
                    score = eyeHealthScore
                )
                ScoreCard(
                    label = "Productivity",
                    score = productivityScore
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Daily stats
            dailyWellness?.let { wellness ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Today's Stats",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        StatRow(
                            label = "Screen Time",
                            value = formatScreenTime(wellness.totalScreenTime)
                        )
                        StatRow(
                            label = "Breaks Completed",
                            value = "${wellness.completedBreaks} / ${wellness.totalBreaks}"
                        )
                        StatRow(
                            label = "Water Intake",
                            value = "${wellness.totalWaterIntake} / ${wellness.waterGoal} ml"
                        )
                        StatRow(
                            label = "Exercises",
                            value = "${wellness.exercisesCompleted}"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recommendations
            if (recommendations.isNotEmpty()) {
                Text(
                    text = "AI Recommendations",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))

                recommendations.forEach { recommendation ->
                    RecommendationCard(
                        recommendation = recommendation
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

/**
 * Score card component
 */
@Composable
private fun ScoreCard(
    label: String,
    score: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(0.45f)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$score/100",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Stat row component
 */
@Composable
private fun StatRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Recommendation card component
 */
@Composable
private fun RecommendationCard(
    recommendation: com.eyezen.data.model.Recommendation
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                Icons.Default.TrendingUp,
                contentDescription = null,
                tint = getPriorityColor(recommendation.priority),
                modifier = Modifier.padding(end = 8.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = recommendation.title,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = recommendation.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }
    }
}

/**
 * Format screen time in hours and minutes
 */
private fun formatScreenTime(ms: Long): String {
    val minutes = ms / (1000 * 60)
    val hours = minutes / 60
    val mins = minutes % 60
    return if (hours > 0) "${hours}h ${mins}m" else "${mins}m"
}

/**
 * Get priority color
 */
private fun getPriorityColor(priority: String): Color {
    return when (priority) {
        "high" -> Color(0xFFD32F2F)
        "medium" -> Color(0xFFFFA726)
        "low" -> Color(0xFF66BB6A)
        else -> Color.Gray
    }
}
