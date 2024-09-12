package com.example.promodoro_team_21.frontend

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.promodoro_team_21.timer.PomodoroTimerViewModel

@Composable
fun Timer(
    viewModel: PomodoroTimerViewModel,  // ViewModel, um den Timer zu steuern
    modifier: Modifier = Modifier
) {
    // State-Variable, die die verbleibende Zeit und den Status beobachtet
    val remainingTime by remember { mutableStateOf(viewModel.timeRemaining) }
    val isRunning by remember { mutableStateOf(viewModel.isRunning) }
    val isWorkingPhase by remember { mutableStateOf(viewModel.isWorkingPhase) }

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
                val sweepAngle = 360f * (remainingTime / (if (isWorkingPhase) 25 * 60 * 1000f else 5 * 60 * 1000f))
                val strokeWidth = 30f

                // Zeichnen des vollen Kreises (Hintergrund)
                drawArc(
                    color = Color.Gray.copy(alpha = 0.3f),
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
                    color = if (isWorkingPhase) Color.Blue else Color.Red,
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
                        viewModel.stopTimer()
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
            // Button zum Zurücksetzen des Timers
            IconButton(
                onClick = { viewModel.stopTimer() },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset")
            }
        }
    }
}

// Preview der Timer-Seite
@Preview
@Composable
fun TimerPageScreenPreview() {
    // Timer-Seite mit einem Dummy-ViewModel
    Timer(viewModel = PomodoroTimerViewModel(context = LocalContext.current))
}