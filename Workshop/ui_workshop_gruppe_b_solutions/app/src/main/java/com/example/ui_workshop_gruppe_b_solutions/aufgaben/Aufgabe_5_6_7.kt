package com.example.ui_workshop_gruppe_b_solutions.aufgaben

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph


/* TODO: Aufgabe 5
*   Vervollständigt das Scaffold innerhalb des WorkshopApp Composables. Nutze dafür unsere vorher
*   erstellte BottomNavigationBar und lassen die Funktion dieser erstmal leer. Außerdem wollen wir
*   für den floatingActionButton einen FloatingActionButton nutzen, welcher als onClick-Funktion die
*   onFloatingActionButtonClick-Funktion übernimmt. Als Inhalt soll ein Add-Icon verwendet werden.
*
*   TODO: Aufgabe 6.1
*    Erstellt einen navController mithilfe der rememberNavController-Funktion. Danach erstellen wir
*    einen navGraph m´durch die remeber-Funktion welche wir den navController als Parameter
*    übergeben. Innehalb des Lambdas nutzen wir die createGraph-Funktion, mit der startDestination
*    "tasks". Innerhalb des Funktion Lambdas von createGraph nutzen wir 2-mal die composable-
*    Funktion. Diese geben wir für jeden unserer gewünschten Screens(taskList,timer) ein Route
*    Parameter und innerhalb der Lambdas nutzen wir das Composable was wir für die jeweilige Route
*    zeigen wollen.
*    Aufgabe 6.2
*    Implementiert den NavHost innerhalb des Scaffolds. Als navController und navGraph übergeb wir
*    die vorher erstellten und als modifier legen wir ein Padding fest mit dem Wert it. Außerdem
*    implementieren wir für unsere BottomNavigation Buttons mithilfe der navController.navigate()-
*    Funktion, zu welchen Screen wir mit welchem Button navigieren.
 */

/*TODO: Aufgabe 7
*   Hoistet den State aus der Liste aus Aufgabe 2 komplett heraus.
 */




@Composable
fun WorkshopApp(
    tasks: List<String>,
    onChecked:(Boolean)->Unit,
    hours:Int,
    minutes:Int,
    seconds:Int,
    onPlay:()->Unit,
    onReset:()->Unit,
    onFloatingActionButtonClick:()->Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navGraph = remember(navController){
        navController.createGraph("taskList"){
            composable(route="taskList"){
                TaskList(
                    taskList = tasks,
                    onChecked = onChecked
                )
            }
            composable(route="timer"){
                Timer(
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds,
                    onPlay = onPlay,
                    onReset = onReset)
            }
        }
    }
    Scaffold(
        bottomBar = {BottomNavigationBar(
            onListClick = {
                navController.navigate("taskList"){launchSingleTop = true}
            },
            onPlayClick = {
                navController.navigate("timer"){launchSingleTop = true}
            })},
        floatingActionButton = {
            FloatingActionButton(onClick = onFloatingActionButtonClick) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
            }
        }
    ) {
        NavHost(
            navController = navController,
            graph = navGraph,
            modifier = Modifier.padding(it))
    }
}

@Preview
@Composable
private fun WorkshopAppPreview() {
    val taskList = List(30){
        ""
    }
    val takListState = remember {
        taskList.toMutableStateList()
    }
    WorkshopApp(
        tasks = takListState,
        onChecked = {},
        hours = 12,
        minutes = 12,
        seconds = 12,
        onPlay = {},
        onReset = {},
        onFloatingActionButtonClick = {})
}

