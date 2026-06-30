package com.eyezen.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Score badge component.
 *
 * Displays a circular score with label.
 */
@Composable
fun ScoreBadge(
    score: Int,
    maxScore: Int = 100,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = getScoreColor(score, maxScore),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "$score/$maxScore",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

/**
 * Get color based on score percentage.
 */
private fun getScoreColor(
    score: Int,
    maxScore: Int
): androidx.compose.ui.graphics.Color {
    val percentage = (score.toFloat() / maxScore) * 100
    return when {
        percentage >= 80 -> androidx.compose.material3.MaterialTheme.colorScheme.primary
        percentage >= 60 -> androidx.compose.ui.graphics.Color(0xFFFFC107)
        else -> androidx.compose.material3.MaterialTheme.colorScheme.error
    }
}
