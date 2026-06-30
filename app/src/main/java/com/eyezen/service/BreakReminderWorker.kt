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
 * Break reminder worker.
 *
 * Sends notification when it's time for a break.
 */
class BreakReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            sendBreakReminder()
            Result.success()
        } catch (e: Exception) {
            Timber.e(e, "Failed to send break reminder")
            Result.retry()
        }
    }

    /**
     * Send break reminder notification
     */
    private fun sendBreakReminder() {
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
            EyeZenApp.CHANNEL_BREAK_REMINDERS
        )
            .setContentTitle("Break Time!")
            .setContentText("Time for your 20-20-20 break")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(1001, notification)
        Timber.d("Break reminder notification sent")
    }
}
