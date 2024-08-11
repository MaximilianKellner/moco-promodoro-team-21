package com.example.promodoro_team_21.frontend

// Import necessary libraries and modules
import android.os.CountDownTimer
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext
import com.example.promodoro_team_21.notifications.TimerNotificationService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Timer(
    modifier: Modifier = Modifier,
    onTimerFinish: () -> Unit // Callback for timer completion
) {
    // Timer duration in milliseconds (25 minutes)
    val timerTime = 5000L // 25 Minuten in Millisekunden  1500L * 1000L

    // State variables to track remaining time and timer status
    var timeLeft by remember { mutableStateOf(timerTime) }
    var isRunning by remember { mutableStateOf(false) }
    var timer: CountDownTimer? = remember { null }
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

    // Function to start the timer
    fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(timeLeft, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
            }

            override fun onFinish() {
                isRunning = false
                timerFinished = true
                onTimerFinish()

            }
        }.start()
        isRunning = true
        timerFinished = false
    }

    // Function to reset the timer
    fun resetTimer() {
        timer?.cancel()
        timeLeft = timerTime // Reset to 25 minutes
        isRunning = false
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
                fontSize = 60.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Button to start the timer
            IconButton(
                onClick = { if (!isRunning) startTimer() },
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play")
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

// Task Composable
@Composable
fun Task(text: String, initialChecked: Boolean, onChecked: (Boolean) -> Unit) {
    var isChecked by remember { mutableStateOf(initialChecked) }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.secondary, shape = RoundedCornerShape(8.dp))
            .clickable {
                isChecked = !isChecked
                onChecked(isChecked)
            }
    ) {
        // Display task text
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        )

        // Checkbox for task completion status
        Checkbox(
            checked = isChecked,
            onCheckedChange = { isSelected ->
                isChecked = isSelected
                onChecked(isSelected)
            },
            colors = CheckboxDefaults.colors(
                checkmarkColor = MaterialTheme.colorScheme.primary,
                checkedColor = MaterialTheme.colorScheme.onPrimary,
                uncheckedColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }
}

// TaskList Composable
@Composable
fun TaskList(taskList: List<String>, onChecked: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val taskStateList = remember {
        taskList.toMutableStateList()
    }
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(taskStateList) { item ->
            Task(text = item, initialChecked = false, onChecked = onChecked)
        }
    }
}

@Composable
fun TimerAndTaskList(
    modifier: Modifier = Modifier,
    onTimerFinish: () -> Unit // Callback for timer completion
) {
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

        TaskList(
            taskList = List(5) { "Task #$it" },
            onChecked = {},
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(16.dp)
                .weight(1f)
        )
    }
}

// Previews for development and testing
@Preview(showBackground = true)
@Composable
fun TimerAndTaskListPreview() {
    lateinit var timerNotificationService: TimerNotificationService
    timerNotificationService = TimerNotificationService(context = LocalContext.current)

    TimerAndTaskList(onTimerFinish = { timerNotificationService.sendNotification()}
    )
}