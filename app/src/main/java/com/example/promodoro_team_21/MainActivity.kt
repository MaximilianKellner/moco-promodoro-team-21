package com.example.promodoro_team_21

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.promodoro_team_21.frontend.TimerAndTaskList
import com.example.promodoro_team_21.ui.theme.Promodoroteam21Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Promodoroteam21Theme {
                Scaffold { innerPadding ->
                    TimerAndTaskList(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimerAndTaskListPreview() {
    Promodoroteam21Theme {
        Scaffold {
            TimerAndTaskList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            )
        }
    }
}