package com.eyezen.data.model

/**
 * Represents authentication state.
 */
sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    object Unauthenticated : AuthState()
    data class Authenticated(val userId: String, val isGuest: Boolean) : AuthState()
    data class Error(val message: String) : AuthState()
}
