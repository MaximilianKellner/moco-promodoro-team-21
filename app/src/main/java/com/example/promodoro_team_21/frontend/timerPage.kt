package com.example.promodoro_team_21.frontend


import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.*
import androidx.compose.material.icons.filled.Pause


@Composable
fun Timer(
    modifier: Modifier = Modifier,
    onTimerFinish: () -> Unit // Callback for timer completion
) {
    // Timer duration in milliseconds (25 minutes)
    val timerTime = 5000L // Beispiel: 5 Sekunden, ändern für Produktion (1500L * 1000L)

    // State variables to track remaining time and timer status
    var timeLeft by remember { mutableStateOf(timerTime) }
    var isRunning by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }
    var timerJob by remember { mutableStateOf<Job?>(null) }
    var timerFinished by remember { mutableStateOf(false) }

    // Derived state to track progress
    val progress by remember { derivedStateOf { 1f - timeLeft / timerTime.toFloat() } }

    // Animation variables
    val infiniteTransition = rememberInfiniteTransition()
    val animatedAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Function to start or resume the timer
    fun startTimer() {
        if (!isRunning || isPaused) {
            timerJob = CoroutineScope(Dispatchers.Main).launch {
                while (timeLeft > 0) {
                    delay(1000L)
                    if (isPaused) continue
                    timeLeft -= 1000L
                }
                if (timeLeft <= 0) {
                    isRunning = false
                    timerFinished = true
                    onTimerFinish()
                }
            }
        }
        isRunning = true
        isPaused = false
        timerFinished = false
    }

    // Function to pause the timer
    fun pauseTimer() {
        isPaused = true
    }

    // Function to reset the timer
    fun resetTimer() {
        timerJob?.cancel()
        timeLeft = timerTime // Reset to original time
        isRunning = false
        isPaused = false
        timerFinished = false
    }

    // Calculate minutes and seconds remaining
    val minutes = (timeLeft / 1000) / 60
    val seconds = (timeLeft / 1000) % 60

    // Layout for the timer UI
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
            // Draw the progress arc
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val sweepAngle = 360 * progress
                val strokeWidth = 30f

                // Draw the full circle (background)
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

                // Draw the progress arc with or without animation
                drawArc(
                    color = if (timerFinished) Color.Green.copy(alpha = animatedAlpha) else if (isRunning) Color.Blue else Color.Red,
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

            // Display the remaining time
            Text(
                text = String.format("%02d:%02d", minutes, seconds),
                fontSize = 50.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Button to start or pause the timer
            IconButton(
                onClick = {
                    if (isRunning && !isPaused) {
                        pauseTimer()
                    } else {
                        startTimer()
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(
                    imageVector = if (isRunning && !isPaused) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isRunning && !isPaused) "Pause" else "Play"
                )
            }
            // Button to reset the timer
            IconButton(
                onClick = { resetTimer() },
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset")
            }
        }
    }
}


// Previews for development and testing
@Preview(showBackground = true)
@Composable
fun TimerAndTaskListPreview() {
    TimerAndTaskList(onTimerFinish = {
        // Dummy implementation for preview
    })
}