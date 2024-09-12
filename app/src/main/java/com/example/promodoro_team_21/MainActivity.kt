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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import com.example.promodoro_team_21.frontend.TimerAndTaskList
import com.example.promodoro_team_21.timer.NotificationViewModel
import com.example.promodoro_team_21.timer.PomodoroTimerViewModel
import com.example.promodoro_team_21.ui.theme.Promodoroteam21Theme

class MainActivity : ComponentActivity() {
    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission was granted
            } else {
                // Permission denied
                // Display a notification or dialog that the permission is needed
            }
        }
        checkAndRequestNotificationPermission()

        setContent {
            Promodoroteam21Theme {
                val context = LocalContext.current
                val notificationViewModel = remember { NotificationViewModel(context) }
                val timerViewModel = remember { PomodoroTimerViewModel(notificationViewModel) }

                Scaffold { innerPadding ->
                    TimerAndTaskList(
                        timerViewModel = timerViewModel,
                        notificationViewModel = notificationViewModel,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        onTimerFinish = {}
                    )
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
    val notificationViewModel = NotificationViewModel(context)
    val timerViewModel = PomodoroTimerViewModel(notificationViewModel)

    Promodoroteam21Theme {
        Scaffold {
            TimerAndTaskList(
                timerViewModel = timerViewModel,
                notificationViewModel = notificationViewModel,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                onTimerFinish = {}
            )
        }
    }
}