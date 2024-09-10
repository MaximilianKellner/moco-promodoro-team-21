package com.example.promodoro_team_21.notifications

import android.content.Context
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.promodoro_team_21.lifecycle.AppLifecycleObserver
import com.example.promodoro_team_21.timer.TimerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationViewModel(context: Context, private val timerViewModel: TimerViewModel) : ViewModel() {

    private val notificationRepository = NotificationRepository(context)
    private val appLifecycleObserver = AppLifecycleObserver(::sendNotification)

    init {
        // Stelle sicher, dass der NotificationChannel erstellt ist
        notificationRepository.createNotificationChannel()
    }

    // Startet den Timer und sendet eine Benachrichtigung, wenn der Timer endet
    fun startTimerAndNotify(isAppInBackground: Boolean) {

        /*
        timerViewModel.startTimer()
        timerViewModel.timeLeft.observeForever { timeLeft ->
            if (timeLeft == 0L) {
                if (isAppInBackground) {
                    sendNotification()
                } else {
                    // Optional: UI-Update im Vordergrund
                }
            }
        }
         */
    }

    // Funktion zum Senden der Benachrichtigung
    private fun sendNotification() {
        notificationRepository.sendNotification(
            "Promodoro Timer",
            "Zeit ist abgelaufen! Mach eine Pause."
        )
    }

    // Geben Sie den LifecycleObserver zurück, um diesen im Activity oder Fragment zu überwachen
    fun getAppLifecycleObserver(): LifecycleObserver {
        return appLifecycleObserver
    }
}