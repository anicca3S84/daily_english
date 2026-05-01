package com.codework.dailyenglish.presentation.components
import androidx.compose.material3.Card
import androidx.compose.ui.Modifier
import com.codework.dailyenglish.presentation.screen.home.model.DailyStudyTime
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
            Spacer(modifier = Modifier.height(16.dp))
            val maxMinutes = weeklyStats.maxOfOrNull { it.minutes } ?: 1

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
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
    val maxBarHeight = 100.dp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (day.minutes > 0) "${day.minutes}" else "",
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = if (day.isToday) MaterialTheme.colorScheme.primary else Color.Gray,
            modifier = Modifier.height(14.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        val barHeight = if (max > 0) (day.minutes.toFloat() / max.toFloat()) else 0f
        val actualHeight = maxBarHeight * barHeight

        Box(
            modifier = Modifier
                .width(12.dp)
                .height(actualHeight.coerceAtLeast(4.dp))
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                .background(
                    if (day.isToday) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = day.dayName,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Normal,
            color = if (day.isToday) MaterialTheme.colorScheme.primary else Color.Gray
        )
    }
}