package com.example.promodoro_team_21.timer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.promodoro_team_21.MainActivity
import com.example.promodoro_team_21.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PomodoroTimerViewModel(private val context: Context) : ViewModel() {

    companion object {
        const val WORK_DURATION = 25 * 60 * 1000L // 25 Minuten in Millisekunden
        const val BREAK_DURATION = 5 * 60 * 1000L // 5 Minuten in Millisekunden
        const val NOTIFICATION_CHANNEL_ID = "pomodoro_channel"
    }

    var timeRemaining: Long = WORK_DURATION
        private set

    var isWorkingPhase: Boolean = true
        private set

    // Öffentlich zugängliche Variable, um den Status des Timers zu beobachten
    var isRunning: Boolean = false
        private set

    private var timerJob: Job? = null

    init {
        createNotificationChannel()
    }

    fun startTimer() {
        if (timerJob?.isActive == true) return // Wenn bereits ein Timer läuft, nichts tun

        isRunning = true
        timerJob = viewModelScope.launch {
            while (timeRemaining > 0) {
                delay(1000L)
                timeRemaining -= 1000L
                updateNotification()
            }
            if (isWorkingPhase) {
                timeRemaining = BREAK_DURATION
            } else {
                timeRemaining = WORK_DURATION
            }
            isWorkingPhase = !isWorkingPhase
            updateNotification()
            startTimer()  // Startet die nächste Phase
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        isRunning = false
    }

    private fun updateNotification() {
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            try {
                val notification = createNotification()
                NotificationManagerCompat.from(context).notify(1, notification)
            } catch (e: SecurityException) {
                // Handle the exception, e.g., log it or show a message to the user
                e.printStackTrace()
            }
        } else {
            // TODO: Handle the case where notifications are not enabled
            // You might want to inform the user or request the permission
        }
    }

    private fun createNotification(): Notification {
        val intent = Intent(context, MainActivity::class.java)

        // Verwende FLAG_IMMUTABLE für den PendingIntent
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val statusText = if (isWorkingPhase) "Working: " else "Break: "
        val timeInMinutes = (timeRemaining / 1000) / 60
        val timeInSeconds = (timeRemaining / 1000) % 60
        val timeFormatted = String.format("%02d:%02d", timeInMinutes, timeInSeconds)

        return NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Pomodoro Timer")
            .setContentText("$statusText $timeFormatted")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Pomodoro Timer"
            val descriptionText = "Pomodoro timer notifications"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
