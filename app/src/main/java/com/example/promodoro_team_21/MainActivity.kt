package com.example.promodoro_team_21

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.example.promodoro_team_21.frontend.PermissionExplanationDialog
import com.example.promodoro_team_21.frontend.TimerAndTaskList
import com.example.promodoro_team_21.ui.theme.Promodoroteam21Theme
import com.example.promodoro_team_21.viewModel.NotificationViewModel
import com.example.promodoro_team_21.viewModel.PomodoroTimerViewModel
import com.example.promodoro_team_21.viewModel.TimerRepository
import android.net.Uri
import android.provider.Settings

class MainActivity : ComponentActivity() {
    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>
    private val showDialog = mutableStateOf(false)
    private var ablehnungsCount = 0 // Zähler für Ablehnungen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialisiere das ViewModel im TimerRepository
        val notificationViewModel = NotificationViewModel(this)
        TimerRepository.timerViewModel = PomodoroTimerViewModel(notificationViewModel)

        notificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission was granted
                ablehnungsCount = 0
                TimerRepository.timerViewModel.startTimerInternal()
            } else {
                // Permission denied
                ablehnungsCount++
                showDialog.value = true
            }
        }

        setContent {
            Promodoroteam21Theme {
                if (showDialog.value) {
                    PermissionExplanationDialog(
                        onDismiss = { showDialog.value = false},
                        ablehnungsCount = ablehnungsCount
                    )
                }

                val timerViewModel = TimerRepository.timerViewModel
                val notificationViewModel = timerViewModel.notificationViewModel

                Scaffold { innerPadding ->
                    TimerAndTaskList(
                        timerViewModel = timerViewModel,
                        notificationViewModel = notificationViewModel,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    )
                }
            }
        }
    }

    fun checkAndRequestNotificationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            TimerRepository.timerViewModel.startTimerInternal()
        }
    }
}