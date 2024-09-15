package com.example.promodoro_team_21.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.promodoro_team_21.R

class TimerForegroundService : Service() {

    private lateinit var timer: CountDownTimer
    private val channelId = "timer_channel"
    private val notificationId = 1

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startTimer()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(25 * 60 * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateNotification(millisUntilFinished)
            }

            override fun onFinish() {
                stopSelf()  // Stoppe den Service, wenn der Timer abgelaufen ist
            }
        }
        timer.start()
    }

    private fun updateNotification(timeRemaining: Long) {
        val minutes = (timeRemaining / 1000) / 60
        val seconds = (timeRemaining / 1000) % 60

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Pomodoro Timer")
            .setContentText(String.format("%02d:%02d verbleibend", minutes, seconds))
            .setSmallIcon(R.drawable.ic_timer)
            .setOngoing(true)  // Hält die Notification aktiv
            .build()

        startForeground(notificationId, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()  // Timer stoppen, wenn der Service zerstört wird
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null  // Kein Binding notwendig
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Pomodoro Timer",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
