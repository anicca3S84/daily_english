package com.codework.dailyenglish.presentation.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.screen.home.model.QuickActionItem

@Composable
fun QuickAccessCard(
    item: QuickActionItem,
    backgroundColor: Color,
    circleColor: Color,
    iconBgColor: Color,
    iconColor: Color,
    arrowBgColor: Color,
    modifier: Modifier = Modifier
) {
    // Tự động phân chia mô tả dựa trên nhãn nút để không làm thay đổi lớp Model của bạn
    val description = when (item.label) {
        "Anicca AI" -> "You'r smart learning assistant"
        "Dictionary" -> "Find word meanings"
        "Saved Word" -> "Review your saved words"
        "Translate" -> "Translate any text"
        else -> ""
    }

    // Tự động gán Vector đẹp mắt thay cho ic_launcher mặc định nếu cần
    val iconVector = when (item.label) {
        "Anicca AI" -> Icons.Default.Face
        "Dictionary" -> Icons.Default.Book
        "Saved Word" -> Icons.Default.Bookmark
        "Translate" -> Icons.Default.Translate
        else -> null
    }

    Card(
        onClick = item.onClick,
        modifier = modifier
            .height(160.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp))
        ) {
            // Vòng tròn trang trí mờ ở góc phải trên cùng
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .offset(x = 40.dp, y = (-20).dp)
                    .background(circleColor, shape = androidx.compose.foundation.shape.CircleShape)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Hộp Icon
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(iconBgColor, shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (iconVector != null) {
                        Icon(
                            imageVector = iconVector,
                            contentDescription = item.label,
                            tint = iconColor,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.label,
                            tint = iconColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                // Tiêu đề & Nút bấm dưới chân thẻ
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                        )
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF64748B),
                                fontSize = 11.sp
                            ),
                            maxLines = 2
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(arrowBgColor, shape = androidx.compose.foundation.shape.CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}