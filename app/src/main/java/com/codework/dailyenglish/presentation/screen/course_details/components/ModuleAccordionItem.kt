package com.codework.dailyenglish.presentation.screen.course_details.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.Spaces
import com.codework.dailyenglish.presentation.theme.AppTextSizes

@Composable
fun ModuleAccordionItem(
    title: String,
    description: String,
    lessonsCount: Int,
    isExpanded: Boolean,
    index: Int,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(300),
        label = "arrowRotation"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(Spaces.Space20),
        color = Color.White,
        border = BorderStroke(
            Spaces.Space1,
            if (isExpanded) AppColors.BlueMain.copy(alpha = 0.3f) else AppColors.GrayBorder
        ),
        shadowElevation = if (isExpanded) Spaces.Space4 else Spaces.Space0
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() }
                    .padding(Spaces.Space16),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(Spaces.Space36)
                        .clip(RoundedCornerShape(Spaces.Space10))
                        .background(if (isExpanded) AppColors.BlueMain else AppColors.AppBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        fontWeight = FontWeight.Bold,
                        color = if (isExpanded) Color.White else AppColors.TextSub
                    )
                }

                Spacer(modifier = Modifier.width(Spaces.Space12))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = AppTextSizes.TextSize15,
                        color = AppColors.TextMain
                    )
                    Text(
                        text = pluralStringResource(
                            id = R.plurals.label_lessons_count,
                            count = lessonsCount,
                            lessonsCount
                        ),
                        fontSize = AppTextSizes.TextSizeLabel,
                        color = AppColors.TextSub,
                        fontWeight = FontWeight.Medium
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Toggle",
                    tint = if (isExpanded) AppColors.BlueMain else AppColors.TextSub,
                    modifier = Modifier.rotate(rotation)
                )
            }
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(animationSpec = tween(300)),
                exit = shrinkVertically(animationSpec = tween(300))
            ) {
                content()
            }
        }
    }
}