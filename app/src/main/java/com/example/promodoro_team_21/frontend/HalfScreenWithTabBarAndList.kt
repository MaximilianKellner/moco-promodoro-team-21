package com.example.promodoro_team_21.frontend

import CustomTabBar
import ToDoItem
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.promodoro_team_21.R

@Composable
fun HalfScreenWithTabBarAndList() {
    var selectedTab by remember { mutableStateOf(0) }

    // To-Do-Listen für jeden Tab
    val uniItems = listOf(
        "St1 Praktikum Aufgabe 3",
        "Lernmaterial durchgehen",
        "Hausaufgaben abgeben"
    )
    val privatItems = listOf(
        "Einkaufen gehen",
        "Freunde treffen",
        "Film schauen"
    )
    val arbeitItems = listOf(
        "E-Mails beantworten",
        "Projektbesprechung vorbereiten",
        "Bericht schreiben"
    )

    // Die To-Do-Items basierend auf dem ausgewählten Tab
    val todoItems = when (selectedTab) {
        0 -> uniItems
        1 -> privatItems
        else -> arbeitItems
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f) // Nimmt 50% des Bildschirms ein
            .background(Color(0xFF202020)) // Setzt den Hintergrund auf #202020
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
                        title = todoItems[index],
                        isChecked = false,
                        onCheckChange = {}
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        // Floating Action Button unten rechts mit colorPrimary
        FloatingActionButton(
            onClick = { /* TODO: Handle FAB click */ },
            modifier = Modifier
                .align(Alignment.BottomEnd) // Unten rechts
                .padding(10.dp),
            containerColor = colorResource(id = R.color.colorPrimary)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add", tint = Color.White)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HalfScreenWithTabBarAndListPreview() {
    HalfScreenWithTabBarAndList()
}
