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
import com.example.promodoro_team_21.view.theme.Promodoroteam21Theme

class MainActivity : ComponentActivity() {
    private lateinit var timerNotificationService: TimerNotificationService
    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        timerNotificationService = TimerNotificationService(this)
        notificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                //TODO Permission was granted
                timerNotificationService.sendNotification()
            } else {
                // Permission denied
                //TODO Benachrichtigung oder Dialog anzeigen, dass die Berechtigung benötigt wird
            }
        }
        //TODO best practice??
        checkAndRequestNotificationPermission()

        setContent {
            Promodoroteam21Theme {
                Scaffold { innerPadding ->
                    TimerAndTaskList(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        onTimerFinish = { timerNotificationService.sendNotification()}
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
    lateinit var timerNotificationService: TimerNotificationService
    timerNotificationService = TimerNotificationService(context = LocalContext.current)

    Promodoroteam21Theme {
        Scaffold {
            TimerAndTaskList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                        onTimerFinish = { timerNotificationService.sendNotification()}

            )
        }
    }
}