package com.example.promodoro_team_21.ui.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFFFFF), //Schrift weiß
    secondary = Color(0x991B1D2E), //buttons (pause,play, settings)
    background = Color(0xD700050F), //Hintergrund Timer und Leiste
    surface = Color(0xB4363A55),//TASKS, buttons (pause,play, settings)
    onPrimary = Color(0xFF264BCD), //Bogen, Secondary Buttons
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun Promodoroteam21Theme(
    darkTheme: Boolean = true, // Always use dark theme
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
