package com.example.promodoro_team_21.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.app.PendingIntent
import androidx.core.app.NotificationCompat
import com.example.promodoro_team_21.MainActivity
import com.example.promodoro_team_21.R

class TimerForegroundService : Service() {

    private lateinit var timer: CountDownTimer
    private val channelId = "timer_channel"
    private val notificationId = 1
    private lateinit var sharedPreferences: SharedPreferences

    private var timeRemaining: Long = 25 * 60 * 1000L
    private var isWorkingPhase: Boolean = true
    private var isRunning: Boolean = false

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences("PomodoroTimerPrefs", Context.MODE_PRIVATE)
        loadTimerState()
        createNotificationChannel()
        startTimer()
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeRemaining, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Broadcast the remaining time to the app
                timeRemaining = millisUntilFinished
                saveTimerState()
                val intent = Intent("UpdateTimer")
                intent.putExtra("remainingTime", millisUntilFinished)
                sendBroadcast(intent)
                updateNotification(millisUntilFinished)
            }

            override fun onFinish() {
                // Wechseln Sie zwischen Arbeits- und Pausenphase
                timeRemaining = if (isWorkingPhase) 25 * 60 * 1000L else 5 * 60 * 1000L
                isWorkingPhase = !isWorkingPhase
                saveTimerState()
                startTimer()  // Starten Sie den Timer für die neue Phase neu
            }
        }
        timer.start()
        isRunning = true
    }

    private fun updateNotification(millisUntilFinished: Long) {
        val minutes = (millisUntilFinished / 1000) / 60
        val seconds = (millisUntilFinished / 1000) % 60

        // Fügen Sie einen PendingIntent zur MainActivity hinzu
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        // Überprüfen Sie das API-Level und fügen Sie das richtige Flag hinzu
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Pomodoro Timer")
            .setContentText(String.format("%02d:%02d verbleibend", minutes, seconds))
            .setSmallIcon(R.drawable.ic_timer)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()

        startForeground(notificationId, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
        isRunning = false
        saveTimerState()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
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

    private fun saveTimerState() {
        with(sharedPreferences.edit()) {
            putLong("timeRemaining", timeRemaining)
            putBoolean("isWorkingPhase", isWorkingPhase)
            putBoolean("isRunning", isRunning)
            apply()
        }
    }

    private fun loadTimerState() {
        timeRemaining = sharedPreferences.getLong("timeRemaining", 25 * 60 * 1000L)
        isWorkingPhase = sharedPreferences.getBoolean("isWorkingPhase", true)
        isRunning = sharedPreferences.getBoolean("isRunning", false)
    }
}