package com.example.ui_workshop_gruppe_b_solutions.aufgaben

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

/*TODO: Aufgabe 2.1
*  Vervollständige das TaskList-Composable. Nutzt dafür eine Lazy-Column, welche das taskList-
*  Parameter nutzt aus jedem Listen Eintrag einen Task zu erstellen. Dafür nutzt man die
*  Erweiterungsfunktion items(liste){item->}, mithilfe wessen man für jedes Listenelement eine
*  Operation auszuführen(z.B. ein Composable erstellen). Außerdem soll aus der Liste zuerst eine
*  stateListe gemacht werden durch die toMutableStateList()-Funktion von der Liste.
 */

@Composable
fun TaskList(
    taskList:List<String>,
    onChecked:(Boolean)-> Unit,
    modifier: Modifier = Modifier
) {
    val taskStateList = remember {
        taskList.toMutableStateList()
    }
    LazyColumn (
        modifier = modifier
    ){
        items(taskStateList){
            item->
            Task(text = item, initialChecked = false, onChecked = onChecked)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TaskListPreview() {
    TaskList(taskList = List(15){"$it"}, onChecked = {})
}