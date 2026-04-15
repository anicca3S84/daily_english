package com.codework.dailyenglish.presentation.screen.courses.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.screen.courses.model.CourseUi
import com.codework.dailyenglish.presentation.theme.Colors
import com.codework.dailyenglish.presentation.theme.Spaces

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseCard(
    course: CourseUi,
    modifier: Modifier = Modifier,
    isLocked: Boolean = false,
    isCompleted: Boolean = false,
    onClick: () -> Unit = {},
) {
    val progressPercent = (course.progress * 100).toInt()

    val safeProgress = course.progress.coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = safeProgress,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label = "progress"
    )

    val activeColor = if (isLocked) Color.Gray else course.themeColor.primaryColor

    Card(
        onClick = onClick,
        modifier = modifier.height(Spaces.Space165),
        shape = RoundedCornerShape(Spaces.Space20),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = Spaces.Space2)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .width(Spaces.Space40)
                    .fillMaxHeight()
                    .background(activeColor.copy(alpha = 0.1f))
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .width(Spaces.Space40)
                    .padding(top = Spaces.Space16),
                contentAlignment = Alignment.TopCenter
            ) {
                when {
                    isCompleted -> Icon(
                        Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50)
                    )
                    isLocked -> Icon(
                        Icons.Rounded.Lock,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = Spaces.Space16, top = Spaces.Space16, bottom = Spaces.Space16, end = Spaces.Space48)
                    .alpha(if (isLocked) 0.6f else 1f)
            ) {
                Icon(
                    imageVector = course.icon,
                    contentDescription = null,
                    tint = activeColor,
                    modifier = Modifier.size(Spaces.Space36)
                )

                Spacer(modifier = Modifier.height(Spaces.Space12))

                Text(
                    text = course.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(Spaces.Space2))

                Text(
                    text = course.description,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${course.lessons} " + stringResource(R.string.label_lessons),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                    Text(
                        text = "$progressPercent%",
                        style = MaterialTheme.typography.labelSmall,
                        color = activeColor,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(Spaces.Space8))

                LinearProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Spaces.Space7)
                        .clip(RoundedCornerShape(50)),
                    color = activeColor,
                    trackColor = Colors.TrackColorProgress
                )
            }
        }
    }
}