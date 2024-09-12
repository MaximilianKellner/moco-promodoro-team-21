package com.example.promodoro_team_21.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class old_TimerViewModel : ViewModel() {
    // Timer-Einstellungen (25 Minuten und 5 Minuten Pause)
    private val workTime = 1500L * 1000L // 25 Minuten in Millisekunden
    private val shortBreakTime = 5L * 60L * 1000L // 5 Minuten in Millisekunden
    private val longBreakTime = 15L * 60L * 1000L // 15 Minuten in Millisekunden

    private var isBreakTime = false
    private var isLongBreakTime = false
    private var intervalCount = 0

    // Timer-Zustände
    private val _timeLeft = MutableStateFlow(workTime)
    val timeLeft: StateFlow<Long> get() = _timeLeft

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> get() = _isRunning

    private val _isPaused = MutableStateFlow(false)
    val isPaused: StateFlow<Boolean> get() = _isPaused

    private val _timerFinished = MutableStateFlow(false)
    val timerFinished: StateFlow<Boolean> get() = _timerFinished

    private var timerJob: Job? = null

    // Timer starten
    fun startTimer() {
        if (_isRunning.value && !_isPaused.value) return

        _isPaused.value = false
        _isRunning.value = true
        _timerFinished.value = false

        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000L)
                _timeLeft.value -= 1000L
            }
            onTimerFinish()
        }
    }

    // Timer pausieren
    fun pauseTimer() {
        _isPaused.value = true
        _isRunning.value = false
        timerJob?.cancel()
    }

    // Timer zurücksetzen
    fun resetTimer() {
        timerJob?.cancel()
        _timeLeft.value = if (isBreakTime) shortBreakTime else workTime
        _isRunning.value = false
        _isPaused.value = false
        _timerFinished.value = false
    }

    // Funktion für das Ende des Timers
    private fun onTimerFinish() {
        _isRunning.value = false
        _timerFinished.value = true
        intervalCount++

        if (!isBreakTime) {
            isBreakTime = true
            _timeLeft.value = if (intervalCount % 4 == 0) longBreakTime else shortBreakTime
        } else {
            isBreakTime = false
            _timeLeft.value = workTime
        }
    }
}