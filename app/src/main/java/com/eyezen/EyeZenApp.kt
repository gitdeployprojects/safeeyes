package com.eyezen

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * EyeZen Application class.
 *
 * Initializes:
 * - Hilt dependency injection
 * - Timber logging
 * - Notification channels
 */
@HiltAndroidApp
class EyeZenApp : Application() {

    companion object {
        const val CHANNEL_BREAK_REMINDERS = "break_reminders"
        const val CHANNEL_WATER_REMINDERS = "water_reminders"
        const val CHANNEL_STRETCH_REMINDERS = "stretch_reminders"
        const val CHANNEL_GENERAL = "general"
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize Timber logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Create notification channels
        createNotificationChannels()

        Timber.d("EyeZen App initialized")
    }

    /**
     * Create notification channels for Android 8.0+
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            // Break reminders channel
            val breakChannel = NotificationChannel(
                CHANNEL_BREAK_REMINDERS,
                "Break Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for eye break reminders"
            }
            notificationManager.createNotificationChannel(breakChannel)

            // Water reminders channel
            val waterChannel = NotificationChannel(
                CHANNEL_WATER_REMINDERS,
                "Water Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for water intake reminders"
            }
            notificationManager.createNotificationChannel(waterChannel)

            // Stretch reminders channel
            val stretchChannel = NotificationChannel(
                CHANNEL_STRETCH_REMINDERS,
                "Stretch Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for stretch exercise reminders"
            }
            notificationManager.createNotificationChannel(stretchChannel)

            // General channel
            val generalChannel = NotificationChannel(
                CHANNEL_GENERAL,
                "General",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "General notifications"
            }
            notificationManager.createNotificationChannel(generalChannel)
        }
    }
}
