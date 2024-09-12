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
import com.example.promodoro_team_21.timer.PomodoroTimerViewModel

@Composable
fun Timer(
    viewModel: PomodoroTimerViewModel,  // ViewModel, um den Timer zu steuern
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit  // Callback für den Settings-Button
) {
    // Verwenden von observeAsState, um LiveData im Composable zu überwachen
    val remainingTime by viewModel.timeRemaining.observeAsState(viewModel.timeRemaining.value ?: 0L)
    val isRunning by viewModel.isRunning.observeAsState(viewModel.isRunning.value ?: false)
    val isWorkingPhase by viewModel.isWorkingPhase.observeAsState(viewModel.isWorkingPhase.value ?: true)

    // Layout für die Timer UI
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
            // Zeichnen des Fortschrittsbogens
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val totalDuration = if (isWorkingPhase) PomodoroTimerViewModel.WORK_DURATION else PomodoroTimerViewModel.BREAK_DURATION
                val sweepAngle = 360f * (remainingTime / totalDuration.toFloat())
                val strokeWidth = 30f

                // Zeichnen des vollen Kreises (Hintergrund)
                drawArc(
                    color = Color.Blue.copy(alpha = 0.3f),
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
                    color = Color.Blue,
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

            // Anzeige der verbleibenden Zeit
            Text(
                text = String.format(
                    "%02d:%02d",
                    (remainingTime / 60000),
                    (remainingTime % 60000) / 1000
                ),
                fontSize = 50.sp,
                color = MaterialTheme.colorScheme.primary
            )
            // Text unter dem Countdown, der anzeigt, ob man sich in der Arbeitsphase oder Pausenphase befindet
            Text(
                text = if (isWorkingPhase) "Working" else "Pause",  // Anzeige des Status
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Button zum Starten oder Pausieren des Timers
            IconButton(
                onClick = {
                    if (isRunning) {
                        viewModel.pauseTimer()
                    } else {
                        viewModel.startTimer()
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isRunning) "Pause" else "Play"
                )
            }

            // Button für die Einstellungen
            IconButton(
                onClick = { onSettingsClick() },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
            }

            // Button zum Zurücksetzen des Timers
            IconButton(
                onClick = { viewModel.resetTimer() },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset")
            }
        }
    }
}

