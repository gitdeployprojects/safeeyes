package com.eyezen

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * EyeZen Application class.
 *
 * Initializes:
 * - Timber for logging
 * - Hilt dependency injection
 * - Google Mobile Ads SDK
 * - Notification channels
 */
@HiltAndroidApp
class EyeZenApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Initialize Google Mobile Ads
        MobileAds.initialize(this)

        // Create notification channels
        createNotificationChannels()

        Timber.d("EyeZen initialized successfully")
    }

    /**
     * Creates notification channels for Android 8.0+
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            // Break Reminder Channel
            val breakChannel = NotificationChannel(
                CHANNEL_BREAK_REMINDERS,
                "Break Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for eye break reminders"
                enableVibration(true)
                setShowBadge(true)
            }

            // Water Reminder Channel
            val waterChannel = NotificationChannel(
                CHANNEL_WATER_REMINDERS,
                "Water Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for water intake reminders"
                enableVibration(true)
                setShowBadge(true)
            }

            // Stretch Reminder Channel
            val stretchChannel = NotificationChannel(
                CHANNEL_STRETCH_REMINDERS,
                "Stretch Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for stretch exercises"
                enableVibration(true)
                setShowBadge(true)
            }

            // Daily Summary Channel
            val summaryChannel = NotificationChannel(
                CHANNEL_DAILY_SUMMARY,
                "Daily Summary",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Daily wellness summary notifications"
                setShowBadge(true)
            }

            notificationManager.apply {
                createNotificationChannel(breakChannel)
                createNotificationChannel(waterChannel)
                createNotificationChannel(stretchChannel)
                createNotificationChannel(summaryChannel)
            }
        }
    }

    companion object {
        const val CHANNEL_BREAK_REMINDERS = "break_reminders"
        const val CHANNEL_WATER_REMINDERS = "water_reminders"
        const val CHANNEL_STRETCH_REMINDERS = "stretch_reminders"
        const val CHANNEL_DAILY_SUMMARY = "daily_summary"
    }
}
