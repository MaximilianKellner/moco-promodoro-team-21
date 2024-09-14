import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme

data class TimerSettings(
    val workDuration: Int = 25,
    val breakDuration: Int = 5,
    val longBreakDuration: Int = 15,
    val cyclesBeforeLongBreak: Int = 4
)

object SettingsManager {
    var timerSettings = TimerSettings()

    fun updateSettings(newSettings: TimerSettings) {
        timerSettings = newSettings
    }
}

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val settings = remember { mutableStateOf(SettingsManager.timerSettings) }

    Column {
        Text("Timer Settings", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = settings.value.workDuration.toString(),
            onValueChange = { newValue ->
                val newDuration = newValue.toIntOrNull() ?: settings.value.workDuration
                settings.value = settings.value.copy(workDuration = newDuration)
            },
            label = { Text("Arbeitszeit in Minuten") }
        )

        OutlinedTextField(
            value = settings.value.breakDuration.toString(),
            onValueChange = { newValue ->
                val newDuration = newValue.toIntOrNull() ?: settings.value.breakDuration
                settings.value = settings.value.copy(breakDuration = newDuration)
            },
            label = { Text("Pausen in Miunten") }
        )

        OutlinedTextField(
            value = settings.value.longBreakDuration.toString(),
            onValueChange = { newValue ->
                val newDuration = newValue.toIntOrNull() ?: settings.value.longBreakDuration
                settings.value = settings.value.copy(longBreakDuration = newDuration)
            },
            label = { Text("Langzeitpause in Minuten") }
        )

        OutlinedTextField(
            value = settings.value.cyclesBeforeLongBreak.toString(),
            onValueChange = { newValue ->
                val newCycles = newValue.toIntOrNull() ?: settings.value.cyclesBeforeLongBreak
                settings.value = settings.value.copy(cyclesBeforeLongBreak = newCycles)
            },
            label = { Text("Zyklen vor der Langezeitpause") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            SettingsManager.updateSettings(settings.value)
            onBack() // Gehe zurück zur vorherigen Seite
        }) {
            Text("Änderungen speichern")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen(onBack = {})
}