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
 * Stretch reminder worker.
 *
 * Sends notification to remind user to do stretches.
 */
class StretchReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            sendStretchReminder()
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "Failed to send stretch reminder")
            Result.retry()
        }
    }

    /**
     * Send stretch reminder notification
     */
    private fun sendStretchReminder() {
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
            EyeZenApp.CHANNEL_STRETCH_REMINDERS
        )
            .setContentTitle("Time to Stretch!")
            .setContentText("Do some stretches to loosen up")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1003, notification)
        Timber.d("Stretch reminder notification sent")
    }
}
