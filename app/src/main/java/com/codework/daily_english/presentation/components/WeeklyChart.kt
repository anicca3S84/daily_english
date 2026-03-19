package com.codework.daily_english.presentation.components
import androidx.compose.material3.Card
import androidx.compose.ui.Modifier
import com.codework.daily_english.presentation.screen.home.model.DailyStudyTime
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeeklyProgressCard(weeklyStats: List<DailyStudyTime>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // ... (Phần Title giữ nguyên) ...

            Spacer(modifier = Modifier.height(16.dp))

            val maxMinutes = weeklyStats.maxOfOrNull { it.minutes } ?: 1

            // Row tổng chứa các cột
            Row(
                modifier = Modifier
                    .fillMaxWidth(), // BỎ height(120.dp) ở đây để nó tự co giãn theo nội dung
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                weeklyStats.forEach { day ->
                    BarItem(
                        day = day,
                        max = maxMinutes,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun BarItem(day: DailyStudyTime, max: Int, modifier: Modifier = Modifier) {
    // Định nghĩa chiều cao tối đa CHỈ DÀNH CHO THANH BAR (trừ hao chỗ cho chữ)
    val maxBarHeight = 100.dp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Text số phút (Top)
        Text(
            text = if (day.minutes > 0) "${day.minutes}" else "", // Luôn giữ chỗ kể cả khi rỗng để cột không bị nhảy
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = if (day.isToday) MaterialTheme.colorScheme.primary else Color.Gray,
            modifier = Modifier.height(14.dp) // Cố định chiều cao text
        )

        Spacer(modifier = Modifier.height(4.dp))

        // 2. Cột Bar (Middle)
        // Tính chiều cao cụ thể theo dp thay vì fillMaxHeight
        val barHeight = if (max > 0) (day.minutes.toFloat() / max.toFloat()) else 0f
        val actualHeight = maxBarHeight * barHeight

        Box(
            modifier = Modifier
                .width(12.dp)
                .height(actualHeight.coerceAtLeast(4.dp)) // Dùng .height() thay vì .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                .background(
                    if (day.isToday) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 3. Text Thứ (Bottom)
        Text(
            text = day.dayName,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Normal,
            color = if (day.isToday) MaterialTheme.colorScheme.primary else Color.Gray
        )
    }
}