package com.example.promodoro_team_21.viewModel

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import com.example.promodoro_team_21.MainActivity
import com.example.promodoro_team_21.R

class NotificationViewModel(val context: Context) : ViewModel() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "pomodoro_liveNotification_channel"
        const val NOTIFICATION_CHANNEL_ID_SWITCH_PHASE = "pomodoro_switchPhase_channel"
    }

    init {
        createNotificationChannelLiveTimer()
        createNotificationChannelSwitchPhase() // Erstelle den Switch Phase Notification Channel
    }

    private fun createNotificationChannelLiveTimer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Pomodoro Timer Live"
            val descriptionText = "Live Pomodoro timer notifications"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotificationChannelSwitchPhase() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Switch Phase Notification"
            val descriptionText = "Pomodoro timer notifications when switching phase"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID_SWITCH_PHASE, name, importance).apply {
                description = descriptionText
                setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null)
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createSwitchPhaseNotification(statusText: String): Notification {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID_SWITCH_PHASE)
            .setContentTitle("Pomodoro Timer")
            .setContentText(statusText)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .build()
    }

    fun sendSwitchPhaseNotification(statusText: String) {
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            try {
                val notification = createSwitchPhaseNotification(statusText)
                NotificationManagerCompat.from(context).notify(2, notification)
            } catch (e: SecurityException) {
                e.printStackTrace()
                requestNotificationPermission()
            }
        }
    }

    fun updateLiveNotification(statusText: String, timeFormatted: String) {
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            try {
                val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(statusText)
                    .setContentText(timeFormatted)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .build()

                NotificationManagerCompat.from(context).notify(1, notification)
            } catch (e: SecurityException) {
                e.printStackTrace()
                requestNotificationPermission()
            }
        } else {
            requestNotificationPermission()
        }
    }

    private fun requestNotificationPermission() {
        // Zeige eine Benachrichtigung oder einen Dialog an, um den Benutzer zur Aktivierung der Benachrichtigungsberechtigung aufzufordern
        val intent = Intent().apply {
            action = "android.settings.APP_NOTIFICATION_SETTINGS"
            putExtra("app_package", context.packageName)
            putExtra("app_uid", context.applicationInfo.uid)
            putExtra("android.provider.extra.APP_PACKAGE", context.packageName)
        }
        context.startActivity(intent)
    }
}