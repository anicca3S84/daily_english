package com.codework.dailyenglish.presentation.screen.lessons.lesson.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.domain.lesson.model.LessonStep
import com.codework.dailyenglish.domain.lesson.model.StepData

private val BluePrimary = Color(0xFF2196F3)
private val BlueShadow = Color(0xFF1565C0)

private val GrayDisabled = Color(0xFFE5E5E5)
private val GrayShadow = Color(0xFFCECECE)
private val GrayText = Color(0xFFAFAFAF)

@Composable
fun LessonBottomBar(
    currentStep: LessonStep?,
    isLastStep: Boolean, // Thêm tham số này
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isEnabled = when (val data = currentStep?.data) {
        is StepData.Question -> data.isUserAnswered || data.isAnswerEvaluated
        else -> true
    }

    // Text mặc định sẽ là Finish nếu là bước cuối, ngược lại là Next
    val nextOrFinishText = if (isLastStep) "Finish" else "Next"

    // Logic quyết định text hiển thị trên button
    val buttonText = when (val data = currentStep?.data) {
        is StepData.Question -> if (data.isAnswerEvaluated) nextOrFinishText else "Check"
        else -> nextOrFinishText
    }

    val topColor by animateColorAsState(
        targetValue = if (isEnabled) BluePrimary else GrayDisabled,
        label = "topColor"
    )

    val bottomColor by animateColorAsState(
        targetValue = if (isEnabled) BlueShadow else GrayShadow,
        label = "bottomColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (isEnabled) Color.White else GrayText,
        label = "textColor"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
            contentAlignment = Alignment.Center
        ){
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed by interactionSource.collectIsPressedAsState()
            val topOffset by animateDpAsState(
                targetValue = if (isPressed && isEnabled) 4.dp else 0.dp,
                animationSpec = tween(100), label = "3D_Offset"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .align(Alignment.BottomCenter)
                        .background(bottomColor, RoundedCornerShape(16.dp))
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .offset(y = topOffset)
                        .clip(RoundedCornerShape(16.dp))
                        .background(topColor)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null,
                            enabled = isEnabled,
                            onClick = onNextClick
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = buttonText,
                        color = textColor,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}