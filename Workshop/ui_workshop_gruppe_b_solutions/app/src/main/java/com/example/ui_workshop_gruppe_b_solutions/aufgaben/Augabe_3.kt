package com.example.ui_workshop_gruppe_b_solutions.aufgaben

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/* TODO Aufgabe 3.1
*   Ertselle einen Timer welcher die Zeit darstellt und darunter 2 Buttons besitzt. Nutzt für die
*   Buttons IconButtons, welche als Inhalt jeweils ein PlayArrow- und ein Refresh-Icon nutzen. Als
*   Funktion sollen die entsprechenen Parameter genutzt werden.
 */



@Composable
fun Timer(
    hours: Int,
    minutes: Int,
    seconds: Int,
    onPlay:()->Unit,
    onReset:()->Unit,
    modifier: Modifier = Modifier
) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier=modifier.fillMaxSize()
    ){
        Text(
            text = "$hours:$minutes:$seconds",
            fontSize = 30.sp
        )
        Spacer(modifier = Modifier.size(20.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ){

            IconButton(
                onClick = onPlay,
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "")
            }
            IconButton(
                onClick = onReset,
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = "")
            }
        }
    }
}

@Preview(
    showSystemUi = true
)
@Composable
private fun TimerPreview() {
    Timer(hours = 12, minutes = 12, seconds = 12, onPlay = {}, onReset = {})
}