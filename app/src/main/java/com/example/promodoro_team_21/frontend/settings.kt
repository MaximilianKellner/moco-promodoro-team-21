package com.example.promodoro_team_21.frontend

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.style.TextAlign

data class TimerSettings(
    val workDuration: Int = 25,
    val breakDuration: Int = 5
)

object SettingsManager {
    var timerSettings = TimerSettings()

    fun updateSettings(newSettings: TimerSettings) {
        timerSettings = newSettings
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit,
                   onSettingsChanged: (TimerSettings) -> Unit
) {
    val settings = remember { mutableStateOf(SettingsManager.timerSettings) }
Box(
    modifier = Modifier
        .background(color = MaterialTheme.colorScheme.background)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)

    ) {

        Text(
            text = "Timer Settings",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center, // Zentriere den Textinhalt
            modifier = Modifier
                .fillMaxWidth() // Der Text nimmt die volle Breite ein und wird zentriert
                .padding(10.dp)
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = settings.value.workDuration.toString(),
            onValueChange = { newValue ->
                //Minimum Work sind 5 Minuten
                val newDuration = newValue.toIntOrNull() ?: 5
                settings.value = settings.value.copy(workDuration = newDuration)
            },
            label = { Text("Arbeitszeit in Minuten") },
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = settings.value.breakDuration.toString(),
            onValueChange = { newValue ->
                //Minimum Break ist 1 Minute
                val newDuration = newValue.toIntOrNull() ?: 1
                settings.value = settings.value.copy(breakDuration = newDuration)
            },
            label = { Text("Pausen in Miunten") }
        )


        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Aktion ausführen
                SettingsManager.updateSettings(settings.value)
                onSettingsChanged(settings.value)
                onBack()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimary, // Hintergrundfarbe
                contentColor = MaterialTheme.colorScheme.primary // Textfarbe
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Änderung speichern"
            )
        }
    }
}
}


/*
@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    // Leere Implementierung für das Preview
    SettingsScreen(onBack = {},
        onSettingsChanged = SettingsManager(
            timerSettings = TimerSettings()
        ))
}
*/