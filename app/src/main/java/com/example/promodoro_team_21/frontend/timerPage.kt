package com.example.promodoro_team_21.frontend

// Import necessary libraries and modules
import android.os.Build
import android.os.CountDownTimer
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import com.example.promodoro_team_21.viewModel.TodoViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(taskList) { item ->
            Task(text = item, initialChecked = false, onChecked = onChecked)
        }
    }
}

// TimerAndTaskList Composable
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TimerAndTaskList(
    modifier: Modifier = Modifier,
    onTimerFinish: () -> Unit // Callback for timer completion
) {
    var taskList by remember { mutableStateOf(List(5) { "Task #$it" }) }
    var showDialog by remember { mutableStateOf(false) }
    var newTaskName by remember { mutableStateOf("") }
    val todoVM = TodoViewModel()

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

            TaskList(
                taskList = taskList,
                onChecked = {},
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
                    .padding(16.dp)
                    .weight(1f)
            )
        }

        // Add Task button
        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(35.dp),
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
        }

        // Dialog for adding new task
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "New Task") },
                text = {
                    TextField(
                        value = newTaskName,
                        onValueChange = { newTaskName = it },
                        label = { Text("Task Name") }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            taskList = taskList + newTaskName
                            //Hier sollte die Task der DB hinzugefügt werden
                            //todoVM.addTodo(newTaskName, category)
                            showDialog = false
                            newTaskName = ""
                        }
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            newTaskName = ""
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

// Previews for development and testing
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun TimerAndTaskListPreview() {
    TimerAndTaskList(onTimerFinish = {
        // Dummy implementation for preview
    })
}
