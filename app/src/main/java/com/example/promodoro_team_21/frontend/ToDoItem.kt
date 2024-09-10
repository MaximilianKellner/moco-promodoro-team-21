import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
            .height(48.dp)
            .padding(horizontal = 8.dp)
            .background( colorResource(id = R.color.colorLightest) , shape = RoundedCornerShape(8.dp))
            .clickable {
                //edit todo text edit here
            }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
                .padding(end = 0.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                color = Color.White
            )
            Checkbox(
                checked = isChecked,
                onCheckedChange = {
                    onCheckChange(it)
                    if (it) {
                        onDelete()
                    }
                },
                colors = CheckboxDefaults.colors(
                    checkmarkColor = Color.White,
                    uncheckedColor = colorResource(id = R.color.white), // Color of Checkbox-Border
                    checkedColor = colorResource(id = R.color.colorPrimary)   // Background-Color of Checkbox if checked
                )
            )
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
