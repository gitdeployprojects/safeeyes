package com.eyezen.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.eyezen.ui.screens.auth.AuthScreen
import com.eyezen.ui.screens.home.HomeScreen
import com.eyezen.viewmodel.AuthViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.eyezen.data.model.AuthState

/**
 * Navigation routes
 */
object NavRoutes {
    const val AUTH = "auth"
    const val HOME = "home"
    const val DASHBOARD = "dashboard"
    const val BREAK_HISTORY = "break_history"
    const val WATER_TRACKER = "water_tracker"
    const val SETTINGS = "settings"
}

/**
 * Root navigation for EyeZen.
 *
 * Manages:
 * - Authentication flow
 * - Home dashboard flow
 * - Bottom navigation
 */
@Composable
fun RootNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState = authViewModel.authState.collectAsStateWithLifecycle().value

    val startDestination = when (authState) {
        is AuthState.Authenticated -> NavRoutes.HOME
        else -> NavRoutes.AUTH
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavRoutes.AUTH) {
            AuthScreen(
                authViewModel = authViewModel,
                onAuthSuccess = {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.AUTH) { inclusive = true }
                    }
                }
            )
        }

        composable(NavRoutes.HOME) {
            HomeNavigation(
                navController = navController,
                onSignOut = {
                    authViewModel.signOut()
                    navController.navigate(NavRoutes.AUTH) {
                        popUpTo(NavRoutes.HOME) { inclusive = true }
                    }
                }
            )
        }
    }
}

/**
 * Home section navigation (bottom tab navigation)
 */
@Composable
fun HomeNavigation(
    navController: NavHostController,
    onSignOut: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.DASHBOARD
    ) {
        composable(NavRoutes.DASHBOARD) {
            HomeScreen(onNavigate = { route -> navController.navigate(route) })
        }

        composable(NavRoutes.BREAK_HISTORY) {
            // TODO: Implement BreakHistoryScreen
            HomeScreen(onNavigate = { route -> navController.navigate(route) })
        }

        composable(NavRoutes.WATER_TRACKER) {
            // TODO: Implement WaterTrackerScreen
            HomeScreen(onNavigate = { route -> navController.navigate(route) })
        }

        composable(NavRoutes.SETTINGS) {
            // TODO: Implement SettingsScreen
            HomeScreen(onNavigate = { route -> navController.navigate(route) })
        }
    }
}
