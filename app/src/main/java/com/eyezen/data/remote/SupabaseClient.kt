package com.eyezen.data.remote

import android.content.Context
import io.github.jan_tennert.supabase.SupabaseClient
import io.github.jan_tennert.supabase.auth.Auth
import io.github.jan_tennert.supabase.auth.auth
import io.github.jan_tennert.supabase.createSupabaseClient
import io.github.jan_tennert.supabase.postgrest.Postgrest
import io.github.jan_tennert.supabase.postgrest.postgrest
import timber.log.Timber

/**
 * Supabase client singleton.
 *
 * Manages connection to Supabase backend for:
 * - Authentication
 * - Cloud database (PostgreSQL)
 */
class SupabaseClient private constructor(context: Context) {

    private val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_ANON_KEY
    ) {
        install(Auth)
        install(Postgrest)
    }

    val auth: Auth = client.auth
    val postgrest: Postgrest = client.postgrest

    init {
        Timber.d("Supabase client initialized")
    }

    companion object {
        // Replace with your actual Supabase credentials
        private const val SUPABASE_URL = "https://YOUR_PROJECT.supabase.co"
        private const val SUPABASE_ANON_KEY = "YOUR_ANON_KEY"

        @Volatile
        private var instance: SupabaseClient? = null

        fun getInstance(context: Context): SupabaseClient =
            instance ?: synchronized(this) {
                instance ?: SupabaseClient(context).also { instance = it }
            }
    }
}
