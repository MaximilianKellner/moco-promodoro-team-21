package com.example.ui_workshop_gruppe_b_solutions.aufgaben

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


/*TODO Aufgabe 4
*   Erstellt eine BottomNavigationBar für unsere App. Verfollständige dafür das Composable mit
*   einer BottomAppBar, welche aus zwei IconButtons in einer Reihe besteht. Diese besitzten als Icons jeweils ein
*   List- und ein PlayArrow-Icon. Als Funktion sollen die jeweiligen aus den Parametern übergeben
*   werden.
 */

@Composable
fun BottomNavigationBar(
    onListClick:()-> Unit,
    onPlayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(actions = {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ){
            IconButton(onClick = onListClick,
                modifier = Modifier
                    .weight(1F)
                    .fillMaxSize()) {
                Icon(imageVector = Icons.Default.List, contentDescription = "")
            }
            IconButton(onClick = onPlayClick,
                modifier = Modifier
                    .weight(1F)
                    .fillMaxSize()) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "")
            }
        }
    })
}

@Preview(
    showBackground = true
)
@Composable
private fun BottomNavigationBarPreview() {
    BottomNavigationBar(onListClick = { /*TODO*/ }, onPlayClick = { /*TODO*/ })
}