package com.codework.dailyenglish.presentation.screen.home.components
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.presentation.theme.AppColors

@Composable
fun WeeklyProgressCard(weeklyStats: List<DailyStudyTime>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = AppColors.AppSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp) // Flat design
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Tiêu đề biểu đồ căn giữa
            Text(
                text = "Weekly Study Progress",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleSmall,
                color = AppColors.TitleColor,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            val maxMinutes = weeklyStats.maxOfOrNull { it.minutes } ?: 1

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                weeklyStats.forEach { day ->
                    BarItem(
                        day = day,
                        maxSeconds = maxMinutes,
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
    maxSeconds: Int,
    modifier: Modifier = Modifier
) {
    val maxBarHeight = 100.dp
    val barWidth = 10.dp // 👈 Giảm chiều rộng cột

    val timeValue = if (day.minutes >= 60) (day.minutes / 60) else day.minutes
    val timeUnit = if (day.minutes >= 60) "h" else "m"
    val displayTime = if (day.minutes > 0) "$timeValue$timeUnit" else ""

    val safeMax = if (maxSeconds > 0) maxSeconds else 1
    val ratio = day.minutes.toFloat() / safeMax.toFloat()
    val actualHeight = (maxBarHeight * ratio)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = displayTime,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (day.isToday) AppColors.BluePrimary else AppColors.TextSub,
            modifier = Modifier.height(16.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Track (Cột nền)
        Box(
            modifier = Modifier
                .width(barWidth)
                .height(maxBarHeight)
                .clip(RoundedCornerShape(6.dp)) // Bo nhẹ
                .background(AppColors.Slate100)
        ) {
            // Bar (Cột màu)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .width(barWidth)
                    .height(actualHeight)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        if (day.isToday) AppColors.BluePrimary
                        else AppColors.BluePrimary.copy(alpha = 0.4f)
                    )
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = day.dayName,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Medium,
            color = if (day.isToday) AppColors.BluePrimary else AppColors.TextSub
        )
    }
}