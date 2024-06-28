package com.example.ui_workshop_gruppe_b_solutions.aufgaben

import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role.Companion.Checkbox
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip

// Timer Composable
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
            text = "$hours:$minutes:$seconds",
            fontSize = 30.sp,
            //   style = MaterialTheme.colorScheme.primaryContainer, // Standardtextstil aus dem MaterialTheme
            color = MaterialTheme.colorScheme.onPrimary // Ändert die Textfarbe basierend auf dem Hintergrund von MaterialTheme

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

// TaskList Composable
@Composable
fun Task(text: String, initialChecked: Boolean, onChecked: (Boolean) -> Unit) {
    var isChecked by remember { mutableStateOf(initialChecked) }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .background(
                Color.DarkGray,
                shape = RoundedCornerShape(8.dp)),


        ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)

        )

        // Achten Sie darauf, dass `onCheckedChange` das Update des Zustands bewirkt
        Checkbox(
            checked = isChecked,
            onCheckedChange = { isSelected ->
                isChecked = isSelected
                onChecked(isSelected)
            },
            colors = CheckboxDefaults.colors(
                checkmarkColor = MaterialTheme.colorScheme.onPrimary, // Farbe des Häkchens
                checkedColor = MaterialTheme.colorScheme.onPrimary,   // Farbe des Hintergrunds, wenn Checkbox aktiviert ist
                uncheckedColor = Color.White                      // Farbe des Hintergrunds, wenn Checkbox deaktiviert ist
            )
        )
    }
}


// TimerAndTaskList Composable

@Composable
fun TaskList(
    taskList:List<String>,
    onChecked:(Boolean)-> Unit,
    modifier: Modifier = Modifier
) {
    val taskStateList = remember {
        taskList.toMutableStateList()
    }
    LazyColumn (
        modifier = modifier
    ){
        items(taskStateList){
                item->
            Task(text = item, initialChecked = false, onChecked = onChecked)
        }
    }
}
@Composable
fun TimerAndTaskList() {
    Column(modifier =
    Modifier
        .fillMaxSize()
        .fillMaxWidth(),
        // .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Timer(
            hours = 0,
            minutes = 25,
            seconds = 0,
            onPlay = {},
            onReset = {},
            modifier = Modifier
                .background(Color.Black)
                //.fillMaxSize()
                .padding(16.dp)
                .weight(1f)
        )

        TaskList(
            taskList = List(5) { "Task #$it" },
            onChecked = {},
            modifier = Modifier
                //    .clip(shape = RoundedCornerShape(8.dp))
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