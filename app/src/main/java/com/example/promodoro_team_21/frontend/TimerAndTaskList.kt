package com.example.promodoro_team_21.frontend

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.promodoro_team_21.timer.old_TimerViewModel
import androidx.compose.material3.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.promodoro_team_21.timer.PomodoroTimerViewModel

//Gesamte Timer- und Task-Liste Seite
@Composable
fun TimerAndTaskList(
    modifier: Modifier = Modifier,
    viewModel: old_TimerViewModel = old_TimerViewModel(),
    onTimerFinish: () -> Unit // Callback for timer completion
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Timer(
                viewModel = PomodoroTimerViewModel(context = LocalContext.current),
                onSettingsClick = { TODO("hier  wird settings.kt aufgerufen!") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
            )
            HalfScreenWithTabBarAndList()
        }
    }
}

//Preview of the TimerScreen
@Preview
@Composable
fun TimerScreenPreview() {

    //TimerScreen with a dummy implementation of onTimerFinish
    TimerAndTaskList(onTimerFinish = {
        // Dummy implementation for preview
    })
}