package com.example.promodoro_team_21.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.promodoro_team_21.viewModel.TimerRepository

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val viewModel = TimerRepository.timerViewModel  // Greife auf die Singleton-Instanz zu

        when (action) {
            "ACTION_TOGGLE_TIMER" -> {
                if (viewModel.isRunning.value == true) {
                    viewModel.pauseTimer()  // Pausiere den Timer, wenn er läuft
                } else {
                    viewModel.startTimer()  // Starte den Timer, wenn er pausiert ist
                }
                viewModel.saveTimerState()  // Speichere den Timer-Status
            }
            "ACTION_RESET" -> {
                viewModel.resetTimer()  // Setze den Timer zurück
                viewModel.saveTimerState()  // Speichere den Timer-Status
            }
        }
    }
}