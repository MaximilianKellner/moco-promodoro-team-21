package com.example.ui_workshop_gruppe_b_solutions.aufgaben


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ui_workshop_gruppe_b_solutions.ui.theme.UI_Workshop_Gruppe_B_SolutionsTheme

/*
* TODO: Aufgabe 1.1
*  Vervollständige den unten begonnenen Task. Dieser soll aus einer Checkbox und einem Text
*  bestehen, welche nebeneinander liegen sollen. Nutzt dafür eine Reihe(Row).
*  Der Zustand der Checkbox ,der gezeigte Text,als auch die Funktion für die Checkbox sollen als
*  Parameter übergeben werden.
* TODO Aufgabe 1.2
*  Setzt Modifier ein um den Task so wie im Beispiel aussehen zu lassen. Nutzt dafür den
*  fillMaxWidth(), padding() und background(). Zudem nutzt die Parameter horizontalArrangement, um 
*  die Elemente an den Start der Reihe zu setzten und verticalAlignment, um diese innerhalb der Reihe 
*  zu zentrieren.
* TODO Aufgabe 1.3
*  Vervollständige die unten angegebene StatefulCheckbox. Nutzt dafür rememberSaveable() und
*  mutableStateOf(), um einen Boolean als State zu schaffen. Dieser soll sich innerhalb der
*  onChecked() Funktion umkehren (false->true etc.). Außerdem soll die Funktion onChecked() mit dem
*  checkedState als Parameter ausführen.
*  Danach erstezte die normale Checkbox in Task mit, mit einer StatefulCheckbox.
*/

@Composable
fun Task(
    text:String,
    initialChecked: Boolean,
    onChecked:(Boolean)->Unit,
    modifier: Modifier = Modifier
) {
    Row (
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
    ){
        StatefulCheckbox(initialChecked = initialChecked, onChecked = onChecked)
        Text(text = text)
    }
}

@Composable
fun StatefulCheckbox(
    initialChecked: Boolean,
    onChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var checkedState by rememberSaveable {
        mutableStateOf(initialChecked)
    }
    Checkbox(checked = checkedState, onCheckedChange = {
     checkedState = it
     onChecked(checkedState)
    })
}


@Preview(showSystemUi = true)
@Composable
private fun StatefulCheckboxPreview() {
    StatefulCheckbox(initialChecked = false, onChecked = {})
}


@Preview(
    showSystemUi = true
)
@Composable
private fun TaskPreview() {
        Task(
            text = "Aufgabe",
            initialChecked = false,
            onChecked = {})
}