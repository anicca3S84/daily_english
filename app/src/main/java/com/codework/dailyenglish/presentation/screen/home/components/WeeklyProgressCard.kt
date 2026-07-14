package com.codework.dailyenglish.presentation.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.screen.home.model.DailyStudyTime

@Composable
fun WeeklyProgressCard(weeklyStats: List<DailyStudyTime>) {
    val totalMinutes = weeklyStats.sumOf { it.minutes }
    val maxMinutes = weeklyStats.maxOfOrNull { it.minutes } ?: 1

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.BarChart,
                        contentDescription = null,
                        tint = Color(0xFF6366F1),
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Weekly Study Progress",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                    )
                }

                // Định dạng hiển thị đồng bộ dạng "X h Y phút" hoặc "X phút" cho Badge thống kê
                val displayTotalTime = if (totalMinutes >= 60) {
                    val hours = totalMinutes / 60
                    val minutes = totalMinutes % 60
                    if (minutes > 0) {
                        "$hours h $minutes phút"
                    } else {
                        "$hours h"
                    }
                } else {
                    "$totalMinutes phút"
                }

                Box(
                    modifier = Modifier
                        .background(Color(0xFFF3F0FF), shape = RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "$displayTotalTime Total Time",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6F3AFF),
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                weeklyStats.forEach { day ->
                    BarItem(
                        day = day,
                        maxMinutes = maxMinutes,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun BarItem(
    day: DailyStudyTime,
    maxMinutes: Int,
    modifier: Modifier = Modifier
) {
    val maxBarHeight = 110.dp
    val barWidth = 14.dp

    val timeValue = if (day.minutes >= 60) (day.minutes / 60) else day.minutes
    val timeUnit = if (day.minutes >= 60) "h" else "m"
    val displayTime = if (day.minutes > 0) "$timeValue$timeUnit" else ""

    val safeMax = if (maxMinutes > 0) maxMinutes else 1
    val ratio = (day.minutes.toFloat() / safeMax.toFloat()).coerceIn(0f, 1f)
    val actualHeight = maxBarHeight * ratio

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Chỉ số thời gian học bên trên
        Text(
            text = displayTime,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                color = if (day.isToday) Color(0xFF3B82F6) else Color(0xFF94A3B8)
            ),
            modifier = Modifier.height(16.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .width(barWidth)
                .height(maxBarHeight),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Đường kẻ chấm đứt (dashed line) chạy dọc mờ ảo làm guide phía sau biểu đồ
            androidx.compose.foundation.Canvas(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            ) {
                val strokeWidth = 2f
                val dashPathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                drawLine(
                    color = Color(0xFFE2E8F0),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(0f, size.height),
                    strokeWidth = strokeWidth,
                    pathEffect = dashPathEffect
                )
            }

            // Thanh nền rỗng của ngày học
            Box(
                modifier = Modifier
                    .width(barWidth)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFF1F5F9))
            )

            // Thanh tiến trình thực tế được lấp đầy bằng màu Gradient
            if (day.minutes > 0) {
                Box(
                    modifier = Modifier
                        .width(barWidth)
                        .height(actualHeight)
                        .clip(RoundedCornerShape(50))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = if (day.isToday) {
                                    listOf(Color(0xFF3B82F6), Color(0xFF8B5CF6)) // Gradient Xanh -> Tím như ảnh mẫu
                                } else {
                                    listOf(Color(0xFFCBD5E1), Color(0xFFE2E8F0)) // Màu xám nhạt cho ngày thường
                                }
                            )
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Nhãn thứ trong tuần bên dưới
        Text(
            text = day.dayName,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Medium,
                color = if (day.isToday) Color(0xFF3B82F6) else Color(0xFF94A3B8)
            )
        )
    }
}