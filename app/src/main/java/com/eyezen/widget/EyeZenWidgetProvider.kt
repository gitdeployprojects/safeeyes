package com.eyezen.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.eyezen.R
import timber.log.Timber
import java.time.LocalDate

/**
 * App widget provider for EyeZen.
 *
 * Displays:
 * - Current screen time
 * - Breaks completed
 * - Water intake progress
 * - Quick actions
 */
class EyeZenWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        appWidgetIds?.forEach { appWidgetId ->
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAppWidget(
            context: Context?,
            appWidgetManager: AppWidgetManager?,
            appWidgetId: Int
        ) {
            if (context == null || appWidgetManager == null) return

            val views = RemoteViews(context.packageName, R.layout.eye_zen_widget)

            // Update widget with sample data
            views.setTextViewText(R.id.widget_title, "EyeZen")
            views.setTextViewText(R.id.widget_screen_time, "Screen Time: 4h 30m")
            views.setTextViewText(R.id.widget_breaks, "Breaks: 8/10")
            views.setTextViewText(R.id.widget_water, "Water: 6/8 glasses")

            appWidgetManager.updateAppWidget(appWidgetId, views)
            Timber.d("App widget updated: $appWidgetId")
        }
    }
}
