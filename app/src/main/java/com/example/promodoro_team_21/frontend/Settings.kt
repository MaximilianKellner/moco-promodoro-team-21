package com.example.promodoro_team_21.frontend

import androidx.compose.runtime.Composable

data class TimerSettings(
    val workDuration: Int = 25,
    val breakDuration: Int = 5,
    val longBreakDuration: Int = 15,
    val cyclesBeforeLongBreak: Int = 4
)

object SettingsManager {
    var timerSettings = TimerSettings()

    fun updateSettings(newSettings: TimerSettings) {
        timerSettings = newSettings
    }
}

@Composable
fun settings(){

}