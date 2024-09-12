package com.example.promodoro_team_21

import android.Manifest
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.promodoro_team_21.frontend.TimerAndTaskList
import com.example.promodoro_team_21.notifications.TimerNotificationService
import com.example.promodoro_team_21.ui.theme.Promodoroteam21Theme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.promodoro_team_21.frontend.SettingsScreen

class MainActivity : ComponentActivity() {
    private lateinit var timerNotificationService: TimerNotificationService
    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        timerNotificationService = TimerNotificationService(this)
        notificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted
            } else {
                // Permission denied
            }
        }
        checkAndRequestNotificationPermission()

        setContent {
            Promodoroteam21Theme {
                // Create NavController
                val navController = rememberNavController()

                Scaffold { innerPadding ->
                    NavHost(navController = navController, startDestination = "timer") {
                        composable("timer") {
                            TimerAndTaskList(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                onTimerFinish = {
                                    timerNotificationService.sendNotification("TMP Timer abgelaufen", "TMP Pomodoro Session beendet!")
                                },
                                onSettingsClick = {
                                    navController.navigate("settings")
                                }
                            )
                        }
                        composable("settings") {
                            SettingsScreen(onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }

    private fun checkAndRequestNotificationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TimerAndTaskListPreview() {
    val context = LocalContext.current
    val timerNotificationService = TimerNotificationService(context)

    Promodoroteam21Theme {
        Scaffold { innerPadding ->
            TimerAndTaskList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                onTimerFinish = {
                    timerNotificationService.sendNotification("TMP Timer abgelaufen", "TMP Pomodoro Session beendet!")
                },
                onSettingsClick = {
                    // Dummy action for preview
                }
            )
        }
    }
}