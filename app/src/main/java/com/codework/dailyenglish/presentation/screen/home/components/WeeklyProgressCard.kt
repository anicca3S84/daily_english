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
import com.codework.dailyenglish.presentation.theme.AppColors.MainGradient

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
                    modifier = Modifier.weight(1f), // Tự động co giãn để tránh đẩy Badge tổng ra ngoài màn hình
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
                            color = Color(0xFF1E293B),
                            fontSize = 14.sp // Giảm kích thước chữ tiêu đề xuống 14.sp
                        )
                    )
                }

                // Định dạng hiển thị tinh gọn: "Total: Xh Ym" hoặc "Total: X min"
                val displayTotalTime = if (totalMinutes >= 60) {
                    val hours = totalMinutes / 60
                    val minutes = totalMinutes % 60
                    if (minutes > 0) {
                        "${hours}h ${minutes}m"
                    } else {
                        "${hours}h"
                    }
                } else {
                    "${totalMinutes} min"
                }

                // Badge hiển thị tổng thời gian với kích thước rộng hơn và tô màu Gradient [1]
                Box(
                    modifier = Modifier
                        .background(MainGradient, shape = RoundedCornerShape(50)) // Tô màu gradient [1]
                        .padding(horizontal = 16.dp, vertical = 6.dp) // Tăng horizontal padding lên 16.dp giúp ô to hơn [1]
                ) {
                    Text(
                        text = "Total: $displayTotalTime", // Đơn giản hóa nội dung chỉ còn "Total: + thời gian"
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White, // Đổi sang chữ trắng để tương tương phản tốt trên nền gradient [1]
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

    // Logic mới: Tính toán đầy đủ cả giờ và phút lẻ
    val displayTime = if (day.minutes > 0) {
        if (day.minutes >= 60) {
            val hours = day.minutes / 60
            val remainingMinutes = day.minutes % 60
            if (remainingMinutes > 0) {
                "${hours}h ${remainingMinutes}m" // Viết liền không khoảng trắng để tránh bị xuống dòng khi màn hình hẹp
            } else {
                "${hours}h"
            }
        } else {
            "${day.minutes}m"
        }
    } else {
        ""
    }

    val safeMax = if (maxMinutes > 0) maxMinutes else 1
    val ratio = (day.minutes.toFloat() / safeMax.toFloat()).coerceIn(0f, 1f)
    val actualHeight = maxBarHeight * ratio

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = displayTime,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = if (day.isToday) Color(0xFF3B82F6) else Color(0xFF94A3B8)
            ),
            modifier = Modifier.height(16.dp),
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .width(barWidth)
                .height(maxBarHeight),
            contentAlignment = Alignment.BottomCenter
        ) {
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

            Box(
                modifier = Modifier
                    .width(barWidth)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFF1F5F9))
            )

            if (day.minutes > 0) {
                Box(
                    modifier = Modifier
                        .width(barWidth)
                        .height(actualHeight)
                        .clip(RoundedCornerShape(50))
                        .background(
                            brush = Brush.verticalGradient(
                                colors = if (day.isToday) {
                                    listOf(Color(0xFF3B82F6), Color(0xFF8B5CF6))
                                } else {
                                    listOf(Color(0xFFCBD5E1), Color(0xFFE2E8F0))
                                }
                            )
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = day.dayName,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Medium,
                color = if (day.isToday) Color(0xFF3B82F6) else Color(0xFF94A3B8)
            )
        )
    }
}