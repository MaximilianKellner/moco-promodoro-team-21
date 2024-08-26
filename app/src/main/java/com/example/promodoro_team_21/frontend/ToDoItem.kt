import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ToDoItem(
    title: String,
    isChecked: Boolean,
    onCheckChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 8.dp)
            .background(Color(0xFF333333), shape = RoundedCornerShape(8.dp))
            .clickable { onCheckChange(!isChecked) }
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
                onCheckedChange = onCheckChange,
                colors = CheckboxDefaults.colors(checkmarkColor = Color.White)
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
        onCheckChange = {}
    )
}
