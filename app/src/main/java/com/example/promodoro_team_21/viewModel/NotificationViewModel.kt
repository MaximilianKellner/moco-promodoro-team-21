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
import com.example.promodoro_team_21.broadcastReceiver.NotificationReceiver

class NotificationViewModel(val context: Context) : ViewModel() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "pomodoro_liveNotification_channel"
        //second channel for switch phase notification
        const val NOTIFICATION_CHANNEL_ID_SWITCH_PHASE = "pomodoro_switchPhase_channel"
    }

    init {
        createNotificationChannelLiveTimer()
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

    //function to create a notification for the switch phase
    private fun createNotificationChannelSwitchPhase() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Switch Phase Notification"
            val descriptionText = "Pomodoro timer notifications when switching phase"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID_SWITCH_PHASE, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun updateLiveNotification(statusText: String, timeFormatted: String) {
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            try {
                val notification = createLiveTimerNotification(statusText, timeFormatted)
                NotificationManagerCompat.from(context).notify(1, notification)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    //notification which displays the current time on the timer and is able to pause and reset the timer
    private fun createLiveTimerNotification(statusText: String, timeFormatted: String): Notification {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP // or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val toggleIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = "ACTION_TOGGLE_TIMER"
        }
        val togglePendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context, 1, toggleIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val resetIntent = Intent(context, NotificationReceiver::class.java).apply {
            action = "ACTION_RESET"
        }
        val resetPendingIntent: PendingIntent = PendingIntent.getBroadcast(
            context, 2, resetIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val playPauseText = if (TimerRepository.timerViewModel.isRunning.value == true) "Pause" else "Play"


        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Pomodoro Timer")
            .setContentText("$statusText $timeFormatted")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .addAction(R.drawable.ic_launcher_foreground, playPauseText, togglePendingIntent)  // Dynamischer Play/Pause-Button
            .addAction(R.drawable.ic_launcher_foreground, "Reset", resetPendingIntent)  // Reset-Button
            .build()
    }

    private fun createSwitchPhaseNotification(statusText: String): Notification {
        val intent = Intent(context, MainActivity::class.java).apply {
            // Verhindert, dass die Aktivität erneut gestartet wird, wenn sie bereits existiert
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
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
                //TODO ask user to enable notification permission
            }
        }
    }
}