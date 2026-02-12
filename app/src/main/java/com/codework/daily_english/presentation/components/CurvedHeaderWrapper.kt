// File: presentation/components/CurvedHeaderWrapper.kt
package com.codework.daily_english.presentation.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.codework.daily_english.presentation.theme.MainGradient

@Composable
fun CurvedHeaderWrapper(
    modifier: Modifier = Modifier,
    headerHeight: Dp = 260.dp,   // Chiều cao tổng
    gradientHeight: Dp = 220.dp, // Chiều cao phần nền xanh
    content: @Composable ColumnScope.() -> Unit, // Nội dung bên trong (Title, List Level...)
    bottomCardContent: @Composable RowScope.() -> Unit // Nội dung trong Card nổi (Thống kê)
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(headerHeight)
    ) {
        // 1. NỀN GRADIENT CONG (BASE)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(gradientHeight)
                .background(
                    brush = MainGradient,
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
        )

        // 2. NỘI DUNG CHÍNH (Title, Subtitle,...)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 16.dp),
            content = content
        )

        // 3. CARD NỔI (STATS)
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .height(80.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                bottomCardContent()
            }
        }
    }
}

// Component con hiển thị chỉ số (Dùng chung cho cả XP, Streak, Lesson Done...)
@Composable
fun HeaderStatItem(
    value: String,
    label: String,
    icon: ImageVector,
    iconColor: Color,
    textColor: Color = Color(0xFF263238)
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            color = iconColor.copy(alpha = 0.1f),
            shape = CircleShape,
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}