package com.eyezen.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.eyezen.service.WaterReminderWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit

/**
 * Boot receiver for scheduling reminders on device restart.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED && context != null) {
            Timber.d("Device boot completed, scheduling reminders")
            scheduleWaterReminder(context)
        }
    }

    /**
     * Schedule water reminder
     */
    private fun scheduleWaterReminder(context: Context) {
        val waterReminderRequest = OneTimeWorkRequestBuilder<WaterReminderWorker>()
            .setInitialDelay(1, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "water_reminder",
            androidx.work.ExistingWorkPolicy.KEEP,
            waterReminderRequest
        )

        Timber.d("Water reminder scheduled for 1 hour")
    }
}
