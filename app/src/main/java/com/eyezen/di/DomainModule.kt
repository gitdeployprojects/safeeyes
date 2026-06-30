package com.eyezen.di

import com.eyezen.domain.WellnessAIEngine
import com.eyezen.viewmodel.GamificationViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for domain-level dependencies.
 *
 * Provides:
 * - Wellness AI Engine
 * - Business logic components
 */
@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideWellnessAIEngine(): WellnessAIEngine {
        return WellnessAIEngine()
    }
}
