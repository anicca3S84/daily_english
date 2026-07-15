package com.codework.dailyenglish.presentation.screen.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.R

@Composable
fun WeeklyGoalBanner(
    totalTimeMinutes: Int,
    modifier: Modifier = Modifier,
    onViewProgressClick: () -> Unit = {}
) {
    // 1. Định dạng thời gian hoàn toàn bằng Tiếng Anh (English format)
    val displayTime = if (totalTimeMinutes >= 60) {
        val hours = totalTimeMinutes / 60
        val minutes = totalTimeMinutes % 60
        if (minutes > 0) {
            "${hours}h ${minutes}m"
        } else {
            "${hours}h"
        }
    } else {
        "${totalTimeMinutes} min"
    }

    val timeFontSize = if (displayTime.length > 7) 12.sp else 16.sp

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF4FA4FF),
                            Color(0xFF8B5CF6),
                            Color(0xFFD946EF)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1.5f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Keep it up!",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Image(
                            painter = painterResource(id = R.drawable.ic_muscle),
                            contentDescription = "Muscle Icon",
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Text(
                        text = "You're doing great this week!",
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(84.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.15f), // Tô full nền hình tròn
                            shape = CircleShape
                        )
                        .border(
                            width = 1.dp,
                            color = Color.White.copy(alpha = 0.25f), // Tạo viền mảnh nhẹ bao quanh
                            shape = CircleShape
                        )
                        .padding(4.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text(
                            text = displayTime,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = timeFontSize,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "This Week",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}