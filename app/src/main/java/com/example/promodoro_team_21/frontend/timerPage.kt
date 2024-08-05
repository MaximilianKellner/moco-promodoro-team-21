package com.example.promodoro_team_21.frontend

import android.os.CountDownTimer
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.foundation.clickable
import androidx.compose.ui.platform.LocalContext
import com.example.promodoro_team_21.notifications.TimerNotificationService

@Composable
fun Timer(
    modifier: Modifier = Modifier,
    onTimerFinish: () -> Unit // Callback für Timer-Abschluss
) {

    val timerTime = 5000L // 25 Minuten in Millisekunden  1500L * 1000L

    var timeLeft by remember { mutableStateOf(timerTime) }
    var isRunning by remember { mutableStateOf(false) }
    var timer: CountDownTimer? = remember { null }

    fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(timeLeft, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
            }

            override fun onFinish() {
                isRunning = false

                // Callback-Funktion aufruf für den Timer-Abschluss
                onTimerFinish()
            }
        }.start()
        isRunning = true
    }

    fun resetTimer() {
        timer?.cancel()
        timeLeft = timerTime // Reset to 25 minutes
        isRunning = false
    }

    val minutes = (timeLeft / 1000) / 60
    val seconds = (timeLeft / 1000) % 60

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = String.format("%02d:%02d", minutes, seconds),
            fontSize = 48.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = { if (!isRunning) startTimer() },
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.onPrimary)
            ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play")
            }
            IconButton(
                onClick = { resetTimer() },
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.onPrimary)
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
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        )

        Checkbox(
            checked = isChecked,
            onCheckedChange = { isSelected ->
                isChecked = isSelected
                onChecked(isSelected)
            },
            colors = CheckboxDefaults.colors(
                checkmarkColor = MaterialTheme.colorScheme.onPrimary,
                checkedColor = MaterialTheme.colorScheme.onPrimary,
                uncheckedColor = Color.White
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
fun TimerAndTaskList(modifier: Modifier = Modifier,
                     onTimerFinish: () -> Unit // Callback für Timer-Abschluss hinzugefügt
                     ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Timer(
            modifier = Modifier
                .background(Color.Black)
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

    TimerAndTaskList(onTimerFinish = { timerNotificationService.sendNotification()} )
}