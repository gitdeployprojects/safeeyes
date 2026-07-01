package com.eyezen.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Workspace
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eyezen.ui.screens.analytics.AnalyticsScreen
import com.eyezen.ui.screens.dashboard.DashboardScreen
import com.eyezen.ui.screens.exercises.ExerciseGuideScreen
import com.eyezen.ui.screens.exercises.StretchExercisesScreen
import com.eyezen.ui.screens.gamification.GamificationScreen
import com.eyezen.ui.screens.premium.PremiumScreen
import com.eyezen.ui.screens.settings.SettingsScreen
import com.eyezen.ui.theme.EyeZenTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * Main activity for EyeZen app.
 *
 * Hosts:
 * - Navigation
 * - Bottom navigation bar
 * - Theme
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("MainActivity created")

        setContent {
            EyeZenTheme {
                EyeZenApp()
            }
        }
    }
}

/**
 * Main app composable with navigation
 */
@Composable
fun EyeZenApp() {
    val navController = rememberNavController()
    var selectedTab by remember { mutableStateOf("dashboard") }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Home") },
                    selected = selectedTab == "dashboard",
                    onClick = {
                        selectedTab = "dashboard"
                        navController.navigate("dashboard") {
                            popUpTo("dashboard") { inclusive = true }
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.LocalFireDepartment, contentDescription = "Breaks") },
                    label = { Text("Breaks") },
                    selected = selectedTab == "breaks",
                    onClick = {
                        selectedTab = "breaks"
                        navController.navigate("breaks") {
                            popUpTo("breaks") { inclusive = true }
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Workspace, contentDescription = "Exercises") },
                    label = { Text("Exercises") },
                    selected = selectedTab == "exercises",
                    onClick = {
                        selectedTab = "exercises"
                        navController.navigate("exercises") {
                            popUpTo("exercises") { inclusive = true }
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Achievements") },
                    label = { Text("Achievements") },
                    selected = selectedTab == "achievements",
                    onClick = {
                        selectedTab = "achievements"
                        navController.navigate("achievements") {
                            popUpTo("achievements") { inclusive = true }
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = selectedTab == "settings",
                    onClick = {
                        selectedTab = "settings"
                        navController.navigate("settings") {
                            popUpTo("settings") { inclusive = true }
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("dashboard") {
                DashboardScreen(
                    onStartBreak = { navController.navigate("break") },
                    onViewAnalytics = { navController.navigate("analytics") }
                )
            }
            composable("break") {
                // Break screen placeholder
            }
            composable("analytics") {
                AnalyticsScreen()
            }
            composable("breaks") {
                // Breaks history screen
            }
            composable("exercises") {
                StretchExercisesScreen(
                    onStartExercise = { exercise ->
                        navController.navigate("exercise_guide")
                    }
                )
            }
            composable("exercise_guide") {
                ExerciseGuideScreen(
                    onBack = { navController.popBackStack() }
                )
            }
            composable("achievements") {
                GamificationScreen()
            }
            composable("settings") {
                SettingsScreen(
                    onNavigate = { route -> navController.navigate(route) }
                )
            }
            composable("premium") {
                PremiumScreen()
            }
        }
    }
}
