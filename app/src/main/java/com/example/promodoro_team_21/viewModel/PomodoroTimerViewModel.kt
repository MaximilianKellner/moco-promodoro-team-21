package com.example.promodoro_team_21.viewModel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.*
import com.example.promodoro_team_21.MainActivity
import com.example.promodoro_team_21.frontend.SettingsManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PomodoroTimerViewModel(private val _notificationViewModel: NotificationViewModel) : ViewModel() {
    val notificationViewModel: NotificationViewModel
        get() = _notificationViewModel

    companion object {
        var WORK_DURATION = (SettingsManager.timerSettings.workDuration * 60 * 1000).toLong()
        var BREAK_DURATION = (SettingsManager.timerSettings.breakDuration * 60 * 1000).toLong()
        private const val PREFS_NAME = "PomodoroTimerPrefs"
        private const val KEY_TIME_REMAINING = "timeRemaining"
        private const val KEY_IS_WORKING_PHASE = "isWorkingPhase"
    }

    private val _timeRemaining = MutableLiveData<Long>(WORK_DURATION)
    val timeRemaining: LiveData<Long> = _timeRemaining

    private val _isRunning = MutableLiveData<Boolean>(false)
    val isRunning: LiveData<Boolean> = _isRunning

    private val _isWorkingPhase = MutableLiveData<Boolean>(true)
    val isWorkingPhase: LiveData<Boolean> = _isWorkingPhase

    private var timerJob: Job? = null

    init {
        loadTimerState()
    }

    fun startTimer() {
        if (timerJob?.isActive == true) return  // Wenn bereits ein Timer läuft, nichts tun.

        // Überprüfe und fordere die Benachrichtigungsberechtigung an
        (notificationViewModel.context as MainActivity).checkAndRequestNotificationPermission()
    }

    fun startTimerInternal() {
        _isRunning.value = true
        updateNotification()  // Benachrichtigung aktualisieren, um "Pause"-Schaltfläche anzuzeigen
        timerJob = viewModelScope.launch {
            while ((_timeRemaining.value ?: 0) > 0) {
                delay(1000L)
                _timeRemaining.value = _timeRemaining.value?.minus(1000L)
                updateNotification()
            }
            // Wenn der Timer abgelaufen ist, Phase wechseln und automatisch den Timer neu starten
            switchPhase()
        }
    }

    fun pauseTimer() {
        timerJob?.cancel()  // Pausiert den Timer, ohne die verbleibende Zeit zurückzusetzen
        _isRunning.value = false
        updateNotification()  // Benachrichtigung aktualisieren, um "Play"-Schaltfläche anzuzeigen
        saveTimerState()
    }

    fun resetTimer() {
        timerJob?.cancel()
        _isRunning.value = false
        _timeRemaining.value = WORK_DURATION  // Setze den Timer auf WORK_DURATION zurück
        _isWorkingPhase.value = true // Setze die Arbeitsphase zurück
        updateNotification()
        saveTimerState()
    }

    private fun switchPhase() {
        if (_isWorkingPhase.value == true) {
            // Wechsel in die Pausenphase
            _isWorkingPhase.value = false
            _timeRemaining.value = BREAK_DURATION
            _isRunning.value = false  // Timer stoppen, wenn die Arbeitsphase endet
        } else {
            // Wechsel zurück in die Arbeitsphase
            _isWorkingPhase.value = true
            _timeRemaining.value = WORK_DURATION
            _isRunning.value = false  // Timer stoppen, wenn die Arbeitsphase endet
        }
        //Timer phasen wechsel notification
        sendSwitchPhaseNotification()
        updateNotification()
        saveTimerState()
    }

    private fun updateNotification() {
        val statusText = if (_isWorkingPhase.value == true) "Arbeiten: " else "Pause: "
        val timeInMinutes = (_timeRemaining.value ?: 0) / 60000
        val timeInSeconds = (_timeRemaining.value ?: 0) / 1000 % 60
        val timeFormatted = String.format("%02d:%02d", timeInMinutes, timeInSeconds)

        notificationViewModel.updateLiveNotification(statusText, timeFormatted)
    }

    private fun sendSwitchPhaseNotification() {
        val statusText = if (_isWorkingPhase.value == true) "Arbeitsphase" else "Pausephase"
        notificationViewModel.sendSwitchPhaseNotification(statusText)
    }

    fun updateTimerDurations() {
        WORK_DURATION = (SettingsManager.timerSettings.workDuration * 60 * 1000).toLong()
        BREAK_DURATION = (SettingsManager.timerSettings.breakDuration * 60 * 1000).toLong()
        resetTimer()
    }

    fun saveTimerState() {
        val sharedPreferences = notificationViewModel.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putLong(KEY_TIME_REMAINING, _timeRemaining.value ?: WORK_DURATION)
            putBoolean(KEY_IS_WORKING_PHASE, _isWorkingPhase.value ?: true)
            apply()
        }
    }

    private fun loadTimerState() {
        val sharedPreferences = notificationViewModel.context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        _timeRemaining.value = sharedPreferences.getLong(KEY_TIME_REMAINING, WORK_DURATION)
        _isWorkingPhase.value = sharedPreferences.getBoolean(KEY_IS_WORKING_PHASE, true)
    }
}