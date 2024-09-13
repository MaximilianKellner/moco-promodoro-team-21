package com.example.promodoro_team_21.frontend

import CustomTabBar // Importangabe nicht vergessen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.promodoro_team_21.model.Category
import com.example.promodoro_team_21.viewModel.TodoViewModel
import androidx.compose.ui.graphics.Color
@Composable
fun HalfScreenWithTabBarAndList() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }
    var newTaskName by remember { mutableStateOf("") }
    val todoVM = TodoViewModel()

    val uniItems by todoVM.getTodoByCategory(Category.UNI).observeAsState(emptyList())
    val privatItems by todoVM.getTodoByCategory(Category.PRIVAT).observeAsState(emptyList())
    val arbeitItems by todoVM.getTodoByCategory(Category.ARBEIT).observeAsState(emptyList())

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
            .fillMaxHeight(1f)
            .background(color = MaterialTheme.colorScheme.secondary)
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
                    val item = todoItems[index]
                    var isCheckedState by remember { mutableStateOf(false) }

                    ToDoItem(
                        title = item.title,
                        isChecked = isCheckedState,  // Zustand der Checkbox
                        onCheckChange = { isChecked ->
                            isCheckedState = isChecked
                            // Hier könnte man den Zustand speichern oder andere Aktionen ausführen
                        },
                        onDelete = {
                            todoVM.deleteTodo(item.id) // Lösche das Item, wenn der Mülleimer-Button geklickt wird
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Button zum Hinzufügen einer neuen Aufgabe
        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add", tint = MaterialTheme.colorScheme.primary)
        }

        // Dialog zum Hinzufügen einer neuen Aufgabe
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = "New Task") },
                text = {
                    TextField(
                        value = newTaskName,
                        onValueChange = { newTaskName = it },
                        label = { Text("Task Name") },

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