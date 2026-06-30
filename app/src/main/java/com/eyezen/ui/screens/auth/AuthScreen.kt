package com.eyezen.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.eyezen.data.model.AuthState
import com.eyezen.viewmodel.AuthViewModel
import timber.log.Timber

/**
 * Authentication screen.
 *
 * Allows users to:
 * - Continue as guest
 * - Sign in with Google (TODO)
 */
@Composable
fun AuthScreen(
    authViewModel: AuthViewModel,
    onAuthSuccess: () -> Unit
) {
    val authState by authViewModel.authState.collectAsState(initial = AuthState.Initial)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                Timber.d("Authentication successful")
                onAuthSuccess()
            }
            is AuthState.Error -> {
                snackbarHostState.showSnackbar((authState as AuthState.Error).message)
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo and Title
        Text(
            text = "EyeZen",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "AI Digital Wellness",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Protect your eyes, boost your productivity",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Continue as Guest Button
        Button(
            onClick = {
                authViewModel.continueAsGuest()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = authState !is AuthState.Loading
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(end = 8.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Text("Continue as Guest")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign in with Google Button
        OutlinedButton(
            onClick = {
                // TODO: Implement Google Sign-In
                Timber.d("Google Sign-In clicked")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = authState !is AuthState.Loading
        ) {
            Text("Sign in with Google")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "By continuing, you agree to our Terms of Service and Privacy Policy",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }

    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier.padding(16.dp)
    )
}
