package com.eyezen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eyezen.data.model.AuthState
import com.eyezen.data.repository.AuthRepository
import com.eyezen.data.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for authentication.
 *
 * Manages:
 * - Auth state
 * - Sign in / sign out
 * - User session
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val authState: StateFlow<AuthState> = authRepository.authState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AuthState.Initial
        )

    /**
     * Continue as guest
     */
    fun continueAsGuest() {
        viewModelScope.launch {
            val result = authRepository.continueAsGuest()
            if (result.isSuccess) {
                val userId = result.getOrNull() ?: return@launch
                userPreferencesRepository.initializePreferences(userId)
                Timber.d("Guest login successful")
            } else {
                Timber.e("Guest login failed: ${result.exceptionOrNull()}")
            }
        }
    }

    /**
     * Sign in with Google
     */
    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            val result = authRepository.signInWithGoogle(idToken)
            if (result.isSuccess) {
                val userId = result.getOrNull() ?: return@launch
                userPreferencesRepository.initializePreferences(userId)
                Timber.d("Google sign-in successful")
            } else {
                Timber.e("Google sign-in failed: ${result.exceptionOrNull()}")
            }
        }
    }

    /**
     * Sign out
     */
    fun signOut() {
        viewModelScope.launch {
            val result = authRepository.signOut()
            if (result.isSuccess) {
                Timber.d("Sign out successful")
            } else {
                Timber.e("Sign out failed: ${result.exceptionOrNull()}")
            }
        }
    }
}
