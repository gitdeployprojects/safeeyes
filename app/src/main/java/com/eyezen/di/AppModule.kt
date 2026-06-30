package com.eyezen.di

import android.content.Context
import androidx.room.Room
import com.eyezen.data.local.AppDatabase
import com.eyezen.data.local.dao.BreakDao
import com.eyezen.data.local.dao.UserPreferencesDao
import com.eyezen.data.local.dao.WaterIntakeDao
import com.eyezen.data.remote.SupabaseClient
import com.eyezen.data.repository.AuthRepository
import com.eyezen.data.repository.BreakRepository
import com.eyezen.data.repository.UserPreferencesRepository
import com.eyezen.data.repository.WaterIntakeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for application-wide dependencies.
 *
 * Provides:
 * - Database
 * - DAOs
 * - Repositories
 * - Remote clients
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "eyezen_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideBreakDao(database: AppDatabase): BreakDao {
        return database.breakDao()
    }

    @Provides
    @Singleton
    fun provideUserPreferencesDao(database: AppDatabase): UserPreferencesDao {
        return database.userPreferencesDao()
    }

    @Provides
    @Singleton
    fun provideWaterIntakeDao(database: AppDatabase): WaterIntakeDao {
        return database.waterIntakeDao()
    }

    @Provides
    @Singleton
    fun provideSupabaseClient(
        @ApplicationContext context: Context
    ): SupabaseClient {
        return SupabaseClient.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        supabaseClient: SupabaseClient
    ): AuthRepository {
        return AuthRepository(supabaseClient)
    }

    @Provides
    @Singleton
    fun provideBreakRepository(
        breakDao: BreakDao,
        supabaseClient: SupabaseClient
    ): BreakRepository {
        return BreakRepository(breakDao, supabaseClient)
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(
        userPreferencesDao: UserPreferencesDao,
        supabaseClient: SupabaseClient
    ): UserPreferencesRepository {
        return UserPreferencesRepository(userPreferencesDao, supabaseClient)
    }

    @Provides
    @Singleton
    fun provideWaterIntakeRepository(
        waterIntakeDao: WaterIntakeDao,
        supabaseClient: SupabaseClient
    ): WaterIntakeRepository {
        return WaterIntakeRepository(waterIntakeDao, supabaseClient)
    }
}
