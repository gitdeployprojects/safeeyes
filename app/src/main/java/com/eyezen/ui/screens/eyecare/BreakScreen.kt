package com.eyezen.ui.screens.eyecare

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.eyezen.viewmodel.BreakState
import com.eyezen.viewmodel.EyeCareViewModel
import kotlinx.coroutines.delay
import timber.log.Timber

/**
 * Fullscreen break screen.
 *
 * Displays:
 * - Countdown timer
 * - Exercise instructions
 * - Complete/Skip/Snooze buttons
 */
@Composable
fun BreakScreen(
    eyeCareViewModel: EyeCareViewModel = hiltViewModel(),
    onBreakComplete: () -> Unit = {}
) {
    val breakState by eyeCareViewModel.breakState.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity

    var timeRemaining by remember { mutableStateOf(0) }
    var isRunning by remember { mutableStateOf(true) }

    // Keep screen on and maximize brightness
    DisposableEffect(Unit) {
        activity?.window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            attributes = attributes.apply {
                screenBrightness = 1.0f
            }
        }

        onDispose {
            activity?.window?.apply {
                clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }

    // Update timer
    LaunchedEffect(isRunning, breakState) {
        if (breakState is BreakState.Running && isRunning) {
            val running = breakState as BreakState.Running
            val duration = running.duration
            timeRemaining = duration

            while (timeRemaining > 0 && isRunning) {
                delay(1000L)
                timeRemaining--
            }

            if (timeRemaining <= 0) {
                eyeCareViewModel.completeBreak()
                onBreakComplete()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            Text(
                text = "Eye Break Time",
                style = MaterialTheme.typography.displayLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Timer Circle
            Box(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = if (breakState is BreakState.Running) {
                        val running = breakState as BreakState.Running
                        (timeRemaining.toFloat() / running.duration).coerceIn(0f, 1f)
                    } else {
                        1f
                    },
                    modifier = Modifier.size(200.dp),
                    color = Color.White,
                    strokeWidth = 8.dp
                )

                Text(
                    text = "$timeRemaining",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontSize = 80.sp
                    ),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Instructions
            Text(
                text = "Follow the 20-20-20 rule:\nLook at something 20 feet away for 20 seconds",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                modifier = Modifier.height(60.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Buttons
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Button(
                    onClick = {
                        isRunning = false
                        eyeCareViewModel.completeBreak()
                        onBreakComplete()
                    },
                    modifier = Modifier.size(width = 160.dp, height = 56.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Text("Done")
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { eyeCareViewModel.snoozeBreak(5) },
                    modifier = Modifier.size(width = 160.dp, height = 56.dp)
                ) {
                    Text("Snooze 5m")
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = { eyeCareViewModel.skipBreak() },
                    modifier = Modifier.size(width = 160.dp, height = 56.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null)
                    Text("Skip")
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
