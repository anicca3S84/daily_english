package com.codework.dailyenglish.presentation.screen.course_details.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.codework.dailyenglish.presentation.screen.course_details.model.LessonStatus
import com.codework.dailyenglish.presentation.screen.course_details.model.LessonUi
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppRadius
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import com.codework.dailyenglish.presentation.theme.Spaces

@Composable
fun LessonAccordionItem(
    lesson: LessonUi,
    onClick: () -> Unit
) {
    val isLocked = lesson.status == LessonStatus.LOCKED
    val isCompleted = lesson.status == LessonStatus.COMPLETED

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppRadius.RadiusMedium))
            .clickable(enabled = !isLocked, onClick = onClick)
            .background(
                if (!isLocked && !isCompleted) AppColors.BlueMain.copy(alpha = 0.05f)
                else Color.Transparent
            )
            .padding(Spaces.Space8),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            modifier = Modifier.size(Spaces.Space36),
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(Spaces.Space1, AppColors.GrayBorder)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = if (isLocked) Color.LightGray else AppColors.TextSub,
                    modifier = Modifier
                        .size(Spaces.Space20)
                        .padding(start = Spaces.Space2)
                )
            }
        }

        Spacer(modifier = Modifier.width(Spaces.Space12))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = lesson.title,
                fontWeight = FontWeight.Bold,
                fontSize = AppTextSizes.TextSizeBody,
                color = if (isLocked) AppColors.TextSub.copy(alpha = 0.6f) else AppColors.TextMain
            )
            Text(
                text = lesson.description,
                fontSize = AppTextSizes.TextSizeLabel,
                color = AppColors.TextSub,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spaces.Space2),
            modifier = Modifier.padding(end = Spaces.Space4)
        ) {
            if (isLocked) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color.LightGray,
                    modifier = Modifier.size(Spaces.Space18)
                )
            } else {
                val activeStars = if (isCompleted) 3 else 0

                repeat(3) { index ->
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Star",
                        tint = if (index < activeStars) Color(0xFFFBBF24) else Color(0xFFE2E8F0),
                        modifier = Modifier.size(Spaces.Space16)
                    )
                }
            }
        }
    }
}