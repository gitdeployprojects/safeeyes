package com.eyezen.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.eyezen.EyeZenApp
import com.eyezen.ui.MainActivity
import timber.log.Timber

/**
 * Water reminder worker.
 *
 * Sends notification to remind user to drink water.
 */
class WaterReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            sendWaterReminder()
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "Failed to send water reminder")
            Result.retry()
        }
    }

    /**
     * Send water reminder notification
     */
    private fun sendWaterReminder() {
        val notificationManager = applicationContext.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            applicationContext,
            EyeZenApp.CHANNEL_WATER_REMINDERS
        )
            .setContentTitle("Time to Hydrate!")
            .setContentText("Drink a glass of water now")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1002, notification)
        Timber.d("Water reminder notification sent")
    }
}
