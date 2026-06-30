package com.eyezen.data.repository

import com.eyezen.data.remote.SupabaseClient
import com.eyezen.data.model.AuthState
import io.github.jan_tennert.supabase.auth.providers.Google
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber

/**
 * Authentication repository.
 *
 * Handles user authentication:
 * - Guest login
 * - Google sign-in
 * - Sign out
 * - Session management
 */
class AuthRepository(
    private val supabaseClient: SupabaseClient
) {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: Flow<AuthState> = _authState.asStateFlow()

    /**
     * Continue as guest (anonymous session)
     */
    suspend fun continueAsGuest(): Result<String> {
        return try {
            _authState.value = AuthState.Loading
            // For guest mode, we'll use a temporary local ID
            val guestId = "guest_${System.currentTimeMillis()}"
            _authState.value = AuthState.Authenticated(guestId, isGuest = true)
            Result.success(guestId)
        } catch (e: Exception) {
            Timber.e(e, "Guest login failed")
            _authState.value = AuthState.Error(e.message ?: "Unknown error")
            Result.failure(e)
        }
    }

    /**
     * Sign in with Google
     */
    suspend fun signInWithGoogle(idToken: String): Result<String> {
        return try {
            _authState.value = AuthState.Loading
            // Sign in with Supabase using Google token
            val session = supabaseClient.auth.signInWith(
                Google
            )
            val userId = session?.user?.id ?: throw Exception("No user returned")
            _authState.value = AuthState.Authenticated(userId, isGuest = false)
            Timber.d("Google sign-in successful: $userId")
            Result.success(userId)
        } catch (e: Exception) {
            Timber.e(e, "Google sign-in failed")
            _authState.value = AuthState.Error(e.message ?: "Unknown error")
            Result.failure(e)
        }
    }

    /**
     * Sign out current user
     */
    suspend fun signOut(): Result<Unit> {
        return try {
            supabaseClient.auth.signOut()
            _authState.value = AuthState.Unauthenticated
            Timber.d("Sign out successful")
            Result.success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Sign out failed")
            Result.failure(e)
        }
    }

    /**
     * Get current user ID
     */
    suspend fun getCurrentUserId(): String? {
        return try {
            supabaseClient.auth.currentUserOrNull()?.id
        } catch (e: Exception) {
            Timber.e(e, "Failed to get current user")
            null
        }
    }
}
