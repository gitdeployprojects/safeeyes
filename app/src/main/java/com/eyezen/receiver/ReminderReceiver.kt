package com.eyezen.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import timber.log.Timber

/**
 * Reminder broadcast receiver.
 *
 * Handles reminder notifications and triggers.
 */
class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val reminderType = intent?.getStringExtra("reminder_type") ?: return
        Timber.d("Reminder received: $reminderType")

        when (reminderType) {
            "break" -> handleBreakReminder(context)
            "water" -> handleWaterReminder(context)
            "stretch" -> handleStretchReminder(context)
        }
    }

    private fun handleBreakReminder(context: Context?) {
        Timber.d("Break reminder triggered")
        // Show notification
    }

    private fun handleWaterReminder(context: Context?) {
        Timber.d("Water reminder triggered")
        // Show notification
    }

    private fun handleStretchReminder(context: Context?) {
        Timber.d("Stretch reminder triggered")
        // Show notification
    }
}
