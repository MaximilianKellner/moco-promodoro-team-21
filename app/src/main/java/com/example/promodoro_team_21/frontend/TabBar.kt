import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun CustomTabBar(
    tabs: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        tabs.forEachIndexed { index, title ->
            Column(
                modifier = Modifier
                    .clickable { onTabSelected(index) }
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    color = if (index == selectedTabIndex) MaterialTheme.colorScheme.primary else Color.Gray,
                    fontSize = 18.sp,
                    fontWeight = if (index == selectedTabIndex) FontWeight.Bold else FontWeight.Normal,
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (index == selectedTabIndex) {
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .width(20.dp)
                            .background(color = MaterialTheme.colorScheme.onPrimary, shape = RoundedCornerShape(1.dp))
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTabBarPreview() {
    var selectedTab by remember { mutableStateOf(0) }
    CustomTabBar(
        tabs = listOf("Uni", "Privat", "Arbeit"),
        selectedTabIndex = selectedTab,
        onTabSelected = { selectedTab = it }
    )
}
