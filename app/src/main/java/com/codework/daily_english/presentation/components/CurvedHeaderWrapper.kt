package com.codework.daily_english.presentation.components
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.codework.daily_english.presentation.theme.BluePrimary
import com.codework.daily_english.presentation.theme.IndigoPurple



@Composable
fun CurvedHeaderWrapper(
    modifier: Modifier = Modifier,
    fixedHeaderHeight: Dp = 205.dp,
    backgroundBrush: Brush = Brush.horizontalGradient(
        colors = listOf(BluePrimary, IndigoPurple)
    ),
    content: @Composable ColumnScope.() -> Unit,
    bottomCardContent: @Composable RowScope.() -> Unit
) {
    val cardHalfHeight = 40.dp
    val shadowSpace = 8.dp

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        // 🔵 1. Nền: Nhận Brush từ tham số thay vì fix cứng
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(fixedHeaderHeight)
                .background(
                    brush = backgroundBrush, // SỬ DỤNG THAM SỐ Ở ĐÂY
                    shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                )
        )

        // 🔤 2. Content: Dùng Box với height cố định để căn chỉnh nội dung bên trong
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(fixedHeaderHeight)
                .statusBarsPadding()
                .padding(top = 8.dp)
        ) {
            content()
        }

        // 💳 3. Thẻ Card: Vẫn neo vào đáy của vùng Header cố định
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp)
                .offset(y = cardHalfHeight)
                .fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                bottomCardContent()
            }
        }
    }

    // 💡 Quan trọng: Thêm một Spacer bên ngoài Box để tránh Content bên dưới bị cái Card đè lên
    Spacer(modifier = Modifier.height(cardHalfHeight + 10.dp))
}
@Composable
fun HeaderStatItem(
    value: String,
    label: String,
    icon: ImageVector? = null,
    iconColor: Color? = null,
    textColor: Color = Color(0xFF263238)
) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        // Chỉ hiển thị icon khi có đủ dữ liệu
        if (icon != null && iconColor != null) {
            Surface(
                color = iconColor.copy(alpha = 0.1f),
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))
        }

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