package com.codework.daily_english.presentation.screen.course_details.components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import com.codework.daily_english.presentation.screen.course_details.model.LessonStatus
import com.codework.daily_english.presentation.screen.course_details.model.LessonUi
import com.codework.daily_english.presentation.theme.Spaces
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Lock
import com.codework.daily_english.R
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.codework.daily_english.presentation.theme.Colors
import com.codework.daily_english.presentation.theme.CourseTheme
import com.codework.daily_english.presentation.theme.ElevationLow
import com.codework.daily_english.presentation.theme.ElevationNone
import com.codework.daily_english.presentation.theme.RadiusFull
import com.codework.daily_english.presentation.theme.RadiusLarge
import com.codework.daily_english.presentation.theme.TextSizes

@SuppressLint("DefaultLocale")
@Composable
fun LessonTimelineItem(
    lesson: LessonUi,
    isLastItem: Boolean,
    index: Int,
    onClick: () -> Unit,
    theme: CourseTheme
) {
    val isLocked = lesson.status == LessonStatus.LOCKED
    val alphaValue = if (isLocked) 0.8f else 1f
    val primaryGreen = theme.primaryColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = Spaces.Space16)
            .alpha(alphaValue)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(Spaces.Space40)
        ) {
            Surface(
                modifier = Modifier.size(Spaces.Space36),
                shape = CircleShape,
                border = BorderStroke(Spaces.Space1_5, theme.primaryColor),
                contentColor = Color.White
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (isLocked) {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(Spaces.Space16),
                            tint = theme.primaryColor.copy(alpha = 0.8f)
                        )
                    } else {
                        Text(
                            text = String.format("%02d", index + 1),
                            fontWeight = FontWeight.Bold,
                            fontSize = TextSizes.TextSizeBody,
                            color = theme.primaryColor
                        )
                    }
                }
            }

            if (!isLastItem) {
                Box(
                    modifier = Modifier
                        .width(Spaces.Space2)
                        .weight(1f)
                        .background(Color.LightGray.copy(alpha = 0.5f))
                )
            }
        }

        Spacer(modifier = Modifier.width(Spaces.Space12))

        Card(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = Spaces.Space16)
                .clickable { onClick() },
            shape = RoundedCornerShape(RadiusLarge),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(
                defaultElevation = if (isLocked) ElevationNone else ElevationLow
            )
        ) {
            Column(modifier = Modifier.padding(Spaces.Space16)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = Spaces.Space12)
                    ) {
                        Text(
                            text = lesson.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isLocked) Color.DarkGray else Colors.TitleColor
                        )

                        Text(
                            text = lesson.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.padding(
                                top = Spaces.Space4,
                                bottom = Spaces.Space12
                            )
                        )
                    }

                    val isCompleted = lesson.status == LessonStatus.COMPLETED

                    val badgeBgColor = when {
                        isCompleted -> theme.moduleHeaderBackgroundColor
                        isLocked -> Color.LightGray.copy(alpha = 0.3f)
                        else -> Color.Gray
                    }

                    val badgeContentColor = when {
                        isCompleted -> theme.primaryColor.copy(alpha = 0.7f)
                        isLocked -> Color.Gray
                        else -> Colors.ExpBgColor
                    }

                    Surface(
                        color = badgeBgColor,
                        shape = RoundedCornerShape(Spaces.Space6),
                        modifier = Modifier.padding(top = Spaces.Space4)
                    ) {
                        Row(
                            modifier = Modifier.padding(
                                horizontal = Spaces.Space6,
                                vertical = Spaces.Space4
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.FlashOn,
                                contentDescription = null,
                                tint = badgeContentColor,
                                modifier = Modifier.size(Spaces.Space12)
                            )

                            Spacer(modifier = Modifier.width(Spaces.Space4))

                            Text(
                                text = stringResource(
                                    id = R.string.exp_format,
                                    lesson.expReward
                                ),
                                style = MaterialTheme.typography.labelSmall,
                                color = badgeContentColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spaces.Space16),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    lesson.skills.forEach { skill ->
                        SkillItem(skill, isLocked)
                    }
                }

                Spacer(modifier = Modifier.height(Spaces.Space16))

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text(
                        text = stringResource(
                            id = R.string.progress_format,
                            if (isLocked) 0 else (lesson.progress * 100).toInt()
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray,
                        modifier = Modifier.width(Spaces.Space100)
                    )

                    LinearProgressIndicator(
                        progress = { if (isLocked) 0f else lesson.progress },
                        modifier = Modifier
                            .weight(1f)
                            .height(Spaces.Space8)
                            .clip(RoundedCornerShape(RadiusFull)),
                        color = if (isLocked) Color.LightGray else primaryGreen.copy(alpha = 0.8f),
                        trackColor = Color.LightGray.copy(alpha = 0.3f),
                        strokeCap = StrokeCap.Round
                    )
                }
            }
        }
    }
}