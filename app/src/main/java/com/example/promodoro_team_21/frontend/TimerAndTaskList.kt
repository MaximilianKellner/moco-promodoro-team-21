package com.example.promodoro_team_21.frontend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.promodoro_team_21.viewModel.PomodoroTimerViewModel
import com.example.promodoro_team_21.viewModel.NotificationViewModel

@Composable
fun TimerAndTaskList(
    timerViewModel: PomodoroTimerViewModel,
    notificationViewModel: NotificationViewModel,
    modifier: Modifier = Modifier,
    onPlayPauseClick: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Timer(
                viewModel = timerViewModel,
                onSettingsClick = {},
                onPlayPauseClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
            )
            HalfScreenWithTabBarAndList()
        }
    }
}

@Preview
@Composable
fun TimerScreenPreview() {
    val context = LocalContext.current
    val notificationViewModel = NotificationViewModel(context)
    val timerViewModel = PomodoroTimerViewModel(notificationViewModel)

    TimerAndTaskList(
        timerViewModel = timerViewModel,
        notificationViewModel = notificationViewModel,
        onPlayPauseClick = {
            // Dummy implementation for preview
        }
    )
}