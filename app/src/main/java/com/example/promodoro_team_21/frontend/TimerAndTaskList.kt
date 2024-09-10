package com.example.promodoro_team_21.frontend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.promodoro_team_21.timer.TimerViewModel
import androidx.compose.material3.*
import androidx.compose.ui.tooling.preview.Preview

// TODO eig identisch mit TimerAndTaskList.
@Composable
fun TimerAndTaskList(
    modifier: Modifier = Modifier,
    viewModel: TimerViewModel = TimerViewModel(),
    onTimerFinish: () -> Unit // Callback for timer completion
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Timer(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .padding(16.dp)
                    .weight(1f),
                onTimerFinish = onTimerFinish
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