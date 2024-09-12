package com.example.promodoro_team_21.timer

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TimerService : Service() {

    private var timer: CountDownTimer? = null
    private var isPaused = false
    private var remainingTime: Long = 60000 // 1 Minute (in ms)
    private val _remainingTimeFlow = MutableStateFlow(remainingTime)
    val remainingTimeFlow: StateFlow<Long> get() = _remainingTimeFlow

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "START_TIMER" -> startTimer()
            "PAUSE_TIMER" -> pauseTimer()
            "RESET_TIMER" -> resetTimer()
        }
        return START_STICKY
    }

    private fun startTimer() {
        if (isPaused) {
            startCountDown(remainingTime)
            isPaused = false
        } else {
            startCountDown(60000) // 1 Minute Timer
        }
    }

    private fun startCountDown(timeInMillis: Long) {
        timer?.cancel()
        timer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                _remainingTimeFlow.value = remainingTime
            }

            override fun onFinish() {
                stopSelf()
            }
        }.start()
    }

    private fun pauseTimer() {
        timer?.cancel()
        isPaused = true
    }

    private fun resetTimer() {
        timer?.cancel()
        remainingTime = 60000 // Reset to 1 minute
        _remainingTimeFlow.value = remainingTime
        isPaused = false
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}