package com.example.promodoro_team_21.frontend

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.promodoro_team_21.R

@Composable
fun ToDoItem(
    title: String,
    isChecked: Boolean,
    onCheckChange: (Boolean) -> Unit,
    onDelete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 8.dp)
            .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
            .clickable {
                // Optional: irgendwelche Aktionen beim Klicken auf den ganzen Bereich
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start, // 1. Sicherstellen, dass der Inhalt von links angeordnet ist
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 16.dp, end = 8.dp)
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    onCheckChange(it)
                },
                colors = CheckboxDefaults.colors(
                    checkmarkColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.primary, // Farbe der Checkbox-Rand
                    checkedColor = MaterialTheme.colorScheme.onPrimary // Hintergrundfarbe der Checkbox, wenn aktiviert
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                color = Color.White,
                modifier = Modifier.weight(1f) // 2. Hinzufügen des Gewicht-Modifiers, damit der Text den verbleibenden Raum einnimmt
            )
            IconButton(
                onClick = { onDelete() },
                colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent),
                modifier = Modifier.align(Alignment.CenterVertically) // 3. Sicherstellen, dass der Button vertikal zentriert ist
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ToDoItemPreview() {
    ToDoItem(
        title = "St1 Praktikum Aufgabe 3",
        isChecked = false,
        onCheckChange = {},
        onDelete = {}
    )
}