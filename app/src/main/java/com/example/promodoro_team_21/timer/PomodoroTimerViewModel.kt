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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.promodoro_team_21.MainActivity
import com.example.promodoro_team_21.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PomodoroTimerViewModel(private val context: Context) : ViewModel() {

    companion object {
        const val WORK_DURATION = 10 * 10 * 1000L // 25 Minuten in Millisekunden
        const val BREAK_DURATION = 5 * 60 * 1000L // 5 Minuten in Millisekunden
        const val NOTIFICATION_CHANNEL_ID = "pomodoro_channel"
    }

    private val _timeRemaining = MutableLiveData<Long>(WORK_DURATION)
    val timeRemaining: LiveData<Long> = _timeRemaining

    private val _isRunning = MutableLiveData<Boolean>(false)
    val isRunning: LiveData<Boolean> = _isRunning

    private val _isWorkingPhase = MutableLiveData<Boolean>(true)
    val isWorkingPhase: LiveData<Boolean> = _isWorkingPhase

    private var timerJob: Job? = null

    init {
        createNotificationChannel()
    }

    fun startTimer() {
        if (timerJob?.isActive == true) return  // Wenn bereits ein Timer läuft, nichts tun

        _isRunning.value = true
        timerJob = viewModelScope.launch {
            while (_timeRemaining.value ?: 0 > 0) {
                delay(1000L)
                _timeRemaining.value = _timeRemaining.value?.minus(1000L)
                updateNotification()
            }
            // Wenn Arbeitsphase beendet ist, in Pausenphase wechseln
            if (_isWorkingPhase.value == true) {
                _timeRemaining.value = BREAK_DURATION
            } else {
                _timeRemaining.value = WORK_DURATION
            }
            _isWorkingPhase.value = _isWorkingPhase.value?.not()
            updateNotification()
            startTimer()  // Startet die nächste Phase (Arbeit oder Pause)
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        _isRunning.value = false
        _timeRemaining.value = if (_isWorkingPhase.value == true) WORK_DURATION else BREAK_DURATION
    }

    private fun updateNotification() {
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            try {
                val notification = createNotification()
                NotificationManagerCompat.from(context).notify(1, notification)
            } catch (e: SecurityException) {
                e.printStackTrace()
            }
        }
    }

    private fun createNotification(): Notification {
        val intent = Intent(context, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val statusText = if (_isWorkingPhase.value == true) "Working: " else "Break: "
        val timeInMinutes = (_timeRemaining.value ?: 0) / 60000
        val timeInSeconds = (_timeRemaining.value ?: 0) / 1000 % 60
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
