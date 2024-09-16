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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.promodoro_team_21.frontend.PermissionExplanationDialog
import com.example.promodoro_team_21.frontend.SettingsManager
import com.example.promodoro_team_21.frontend.SettingsScreen
import com.example.promodoro_team_21.frontend.TimerAndTaskList
import com.example.promodoro_team_21.services.TimerForegroundService
import com.example.promodoro_team_21.ui.theme.Promodoroteam21Theme
import com.example.promodoro_team_21.viewModel.NotificationViewModel
import com.example.promodoro_team_21.viewModel.PomodoroTimerViewModel
import com.example.promodoro_team_21.viewModel.TimerRepository

class MainActivity : ComponentActivity() {
    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>
    private val showDialog = mutableStateOf(false)
    private var ablehnungsCount = 0 // Zähler für Ablehnungen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialisiere das ViewModel im TimerRepository
        val notificationViewModel = NotificationViewModel(this)
        val pomodoroTimerViewModel = PomodoroTimerViewModel(notificationViewModel)
        TimerRepository.timerViewModel = pomodoroTimerViewModel

        // Berechtigung für Benachrichtigungen anfordern
        notificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                ablehnungsCount = 0
                TimerRepository.timerViewModel.startTimerInternal()
                startTimerService(this) // Starte den ForegroundService
            } else {
                ablehnungsCount++
                showDialog.value = true
            }
        }

        setContent {
            Promodoroteam21Theme {
                val navController = rememberNavController()

                if (showDialog.value) {
                    PermissionExplanationDialog(
                        onDismiss = { showDialog.value = false },
                        ablehnungsCount = ablehnungsCount
                    )
                }

                NavHost(navController = navController, startDestination = "timer") {
                    composable("timer") {
                        val timerViewModel = TimerRepository.timerViewModel
                        val notificationViewModel = timerViewModel.notificationViewModel

                        Scaffold { innerPadding ->
                            TimerAndTaskList(
                                timerViewModel = timerViewModel,
                                notificationViewModel = notificationViewModel,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                onSettingsClick = {
                                    navController.navigate("settings") // Navigation zur Settings-Seite
                                }
                            )
                        }
                    }

                    // Die Settings-Seite
                    composable("settings") {
                        SettingsScreen(onBack = {
                            navController.popBackStack() // Zurück zum vorherigen Bildschirm
                        },
                            onSettingsChanged = { newSettings ->
                                // Aktualisieren der Einstellungen und des ViewModels
                                SettingsManager.updateSettings(newSettings)
                                pomodoroTimerViewModel.updateTimerDurations()
                            })
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        TimerRepository.timerViewModel.restoreTimerState()
    }

    override fun onResume() {
        super.onResume()
        TimerRepository.timerViewModel.restoreTimerState()
    }

    // Methode zum Starten des TimerForegroundService
    private fun startTimerService(context: Context) {
        val serviceIntent = Intent(context, TimerForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)  // Für Android 8 und höher
        } else {
            context.startService(serviceIntent)  // Für ältere Android-Versionen
        }
    }

    // Berechtigungen für Benachrichtigungen überprüfen und anfordern
    fun checkAndRequestNotificationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            TimerRepository.timerViewModel.startTimerInternal()
            startTimerService(this) // Starte den ForegroundService
        }
    }
}