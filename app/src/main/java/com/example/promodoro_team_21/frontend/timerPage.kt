package com.example.promodoro_team_21.frontend

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults

@Composable
fun Timer(
    hours: Int,
    minutes: Int,
    seconds: Int,
    onPlay: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = String.format("%02d:%02d:%02d", hours, minutes, seconds),
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.height(15.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onPlay,
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.onPrimary)
            ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play")
            }
            IconButton(
                onClick = onReset,
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
            .background(
                Color.DarkGray,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                isChecked = !isChecked
                onChecked(isChecked)
            }
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
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
fun TimerAndTaskList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Timer(
            hours = 0,
            minutes = 25,
            seconds = 0,
            onPlay = {},
            onReset = {},
            modifier = Modifier
                .background(Color.Black)
                .padding(16.dp)
                .weight(1f)
        )

        TaskList(
            taskList = List(5) { "Task #$it" },
            onChecked = {},
            modifier = Modifier
                .background(Color.Gray)
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
    TimerAndTaskList()
}