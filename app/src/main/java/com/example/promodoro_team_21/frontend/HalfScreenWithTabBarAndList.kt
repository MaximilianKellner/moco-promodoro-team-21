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
            .background(colorResource(id = R.color.colorLighter))
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
                    ToDoItem(
                        title = item.title,
                        isChecked = false,  // Initial state for checkbox
                        onCheckChange = { isChecked ->
                        },
                        onDelete = {
                            todoVM.deleteTodo(item.id) // Delete the item when checked
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        //add new task button
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
