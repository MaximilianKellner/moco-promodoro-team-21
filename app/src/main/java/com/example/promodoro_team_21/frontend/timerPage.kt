package com.example.promodoro_team_21.frontend

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.promodoro_team_21.viewModel.PomodoroTimerViewModel
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.promodoro_team_21.viewModel.NotificationViewModel


@Composable
fun Timer(
    viewModel: PomodoroTimerViewModel,  // ViewModel, um den Timer zu steuern
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit,  // Callback für den Settings-Button
    onPlayPauseClick: () -> Unit  // Callback für den Play/Pause-Button
) {
    val remainingTime by viewModel.timeRemaining.observeAsState(viewModel.timeRemaining.value ?: 0L)
    val isRunning by viewModel.isRunning.observeAsState(viewModel.isRunning.value ?: false)
    val isWorkingPhase by viewModel.isWorkingPhase.observeAsState(viewModel.isWorkingPhase.value ?: true)

    // Extrahiere die Farben aus dem aktuellen Theme
    val progressColor = MaterialTheme.colorScheme.onPrimary
    val backgroundColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(30.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val totalDuration = if (isWorkingPhase) PomodoroTimerViewModel.WORK_DURATION else PomodoroTimerViewModel.BREAK_DURATION
                val sweepAngle = 360f * (remainingTime / totalDuration.toFloat())
                val strokeWidth = 30f

                // Zeichnen des vollen Kreises (Hintergrund)
                drawArc(
                    color = backgroundColor, //Hintergrund des arcs
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round
                    ),
                    size = size
                )

                // Zeichnen des Fortschrittsbogens
                drawArc(
                    color = progressColor, //Farbe dess Arcs
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round
                    ),
                    size = size
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = String.format(
                        "%02d:%02d",
                        (remainingTime / 60000),
                        (remainingTime % 60000) / 1000
                    ),
                    fontSize = 40.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = if (isWorkingPhase) "Arbeit" else "Entspannung",
                    fontSize = 15.sp,
                    color = Color(0xFF7b7b7b)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    onPlayPauseClick()
                    if (isRunning) {
                        viewModel.pauseTimer()
                    } else {
                        viewModel.startTimer()
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isRunning) "Pause" else "Play"
                )
            }

            IconButton(
                onClick = { onSettingsClick() },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
            }

            IconButton(
                onClick = { viewModel.resetTimer() },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset")
            }
        }
    }
}

@Preview
@Composable
fun TimerPreview() {
    val context = LocalContext.current
    val viewModel = remember { PomodoroTimerViewModel(_notificationViewModel = NotificationViewModel(context)) }
    Timer(viewModel = viewModel, onSettingsClick = {}, onPlayPauseClick = {})
}