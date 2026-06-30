package com.eyezen.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Reminder service for break notifications.
 *
 * Handles:
 * - Break reminders
 * - Water intake reminders
 * - Stretch reminders
 * - Daily summary notifications
 */
@AndroidEntryPoint
class ReminderService : Service() {

    private val binder = ReminderBinder()

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.d("ReminderService started")
        scheduleReminders()
        return START_STICKY
    }

    /**
     * Schedule all reminders
     */
    private fun scheduleReminders() {
        // Schedule break reminder
        val breakReminderRequest = OneTimeWorkRequestBuilder<BreakReminderWorker>()
            .setInitialDelay(20, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            "break_reminder",
            androidx.work.ExistingWorkPolicy.KEEP,
            breakReminderRequest
        )

        Timber.d("Break reminder scheduled")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("ReminderService destroyed")
    }

    inner class ReminderBinder : Binder() {
        fun getService(): ReminderService = this@ReminderService
    }
}
