package com.example.promodoro_team_21.frontend

import CustomTabBar
import ToDoItem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.promodoro_team_21.R
import com.example.promodoro_team_21.model.Category
import com.example.promodoro_team_21.viewModel.TodoViewModel

@Composable
fun HalfScreenWithTabBarAndList() {
    var selectedTab by remember { mutableStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    var newTaskName by remember { mutableStateOf("") }
    val todoVM = TodoViewModel()

    // To-Do-Listen für jeden Tab
    val uniItems_old = listOf(
        "St1 Praktikum Aufgabe 3",
        "Lernmaterial durchgehen",
        "Hausaufgaben abgeben"
    )

    val privatItems_old = listOf(
        "Einkaufen gehen",
        "Freunde treffen",
        "Film schauen"
    )
    val arbeitItems_old = listOf(
        "E-Mails beantworten",
        "Projektbesprechung vorbereiten",
        "Bericht schreiben"
    )

    val uniItems by todoVM.getTodoByCategory(Category.UNI).observeAsState(emptyList())
    val privatItems by todoVM.getTodoByCategory(Category.PRIVAT).observeAsState(emptyList())
    val arbeitItems by todoVM.getTodoByCategory(Category.ARBEIT).observeAsState(emptyList())


    // Die To-Do-Items basierend auf dem ausgewählten Tab
    val todoItems = when (selectedTab) {
        0 -> uniItems
        1 -> privatItems
        else -> arbeitItems
    }

    val category = when (selectedTab) {
        0 -> Category.UNI
        1 -> Category.PRIVAT
        else -> Category.ARBEIT
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .background(Color(0xFF202020))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            CustomTabBar(
                tabs = listOf("Uni", "Privat", "Arbeit"),
                selectedTabIndex = selectedTab,
                onTabSelected = { selectedTab = it }
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                items(todoItems.size) { index ->
                    ToDoItem(
                        title = todoItems[index].title,
                        isChecked = false,  // TODO checkbox zustand
                        onCheckChange = {}
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = colorResource(id = R.color.colorPrimary)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
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
                            todoVM.addTodo(newTaskName, category)
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

@Preview(showBackground = true)
@Composable
fun HalfScreenWithTabBarAndListPreview() {
    HalfScreenWithTabBarAndList()
}
