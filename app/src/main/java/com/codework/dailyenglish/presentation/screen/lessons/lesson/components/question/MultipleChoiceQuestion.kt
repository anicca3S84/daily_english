package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.codework.dailyenglish.R
import com.codework.dailyenglish.domain.lesson.model.QuestionContent
import com.codework.dailyenglish.domain.lesson.model.StepData
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.AppIcons
import com.codework.dailyenglish.presentation.theme.AppRadius
import com.codework.dailyenglish.presentation.theme.AppTextSizes
import com.codework.dailyenglish.presentation.theme.Spaces

@Composable
fun MultipleChoiceQuestion(
    data: StepData.Question,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val multipleChoiceContent = data.content as? QuestionContent.MultipleChoice
        ?: return

    val isEvaluated = data.isAnswerEvaluated

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.AppBackground)
            .padding(horizontal = Spaces.Space24, vertical = Spaces.Space24)
    ) {
        Text(
            text = multipleChoiceContent.prompt,
            fontSize = AppTextSizes.TextSizeTitle,
            fontWeight = FontWeight.Bold,
            color = AppColors.Slate700,
            modifier = Modifier.padding(bottom = Spaces.Space36)
        )

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(Spaces.Space12)
        ) {
            multipleChoiceContent.options.forEachIndexed { index, option ->
                val isSelected = data.selectedOptionId == option.id
                val isCorrect = option.isCorrect
                val letter = ('A' + index).toString()

                val animatedBgColor by animateColorAsState(
                    targetValue = when {
                        isEvaluated && isCorrect -> AppColors.Emerald50
                        isEvaluated && isSelected && !isCorrect -> AppColors.Rose50
                        !isEvaluated && isSelected -> AppColors.Indigo50
                        else -> Color.White
                    },
                    animationSpec = tween(300),
                    label = "bgColor"
                )

                val animatedBorderColor by animateColorAsState(
                    targetValue = when {
                        isEvaluated && isCorrect -> AppColors.Emerald500
                        isEvaluated && isSelected && !isCorrect -> AppColors.Rose500
                        !isEvaluated && isSelected -> AppColors.Indigo500
                        else -> AppColors.Slate200
                    },
                    animationSpec = tween(300),
                    label = "borderColor"
                )

                val textColor = when {
                    isEvaluated && isCorrect -> AppColors.Emerald600
                    isEvaluated && isSelected && !isCorrect -> AppColors.Rose600
                    !isEvaluated && isSelected -> AppColors.Indigo600
                    else -> AppColors.Slate700
                }

                val letterBgColor = if (isSelected && !isEvaluated) AppColors.Indigo500 else AppColors.Slate100
                val letterTextColor = if (isSelected && !isEvaluated) Color.White else AppColors.Slate500

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(AppRadius.RadiusLarge))
                        .clickable(enabled = !isEvaluated) {
                            onOptionSelected(option.id)
                        },
                    shape = RoundedCornerShape(AppRadius.RadiusLarge),
                    color = animatedBgColor,
                    border = BorderStroke(
                        width = if (isSelected || (isEvaluated && isCorrect)) Spaces.Space2 else Spaces.Space1,
                        color = animatedBorderColor
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(Spaces.Space16),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(Spaces.Space32)
                                .clip(RoundedCornerShape(Spaces.Space8))
                                .background(letterBgColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = letter,
                                color = letterTextColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = AppTextSizes.TextSizeBody
                            )
                        }

                        Spacer(modifier = Modifier.width(Spaces.Space16))

                        Text(
                            text = option.text,
                            modifier = Modifier.weight(1f),
                            fontSize = AppTextSizes.TextSizeMain,
                            fontWeight = if (isSelected || (isEvaluated && isCorrect))
                                FontWeight.Bold else FontWeight.SemiBold,
                            color = textColor
                        )

                        if (isEvaluated) {
                            when {
                                isCorrect -> {
                                    Icon(
                                        imageVector = Icons.Rounded.CheckCircle,
                                        contentDescription = "Correct",
                                        tint = AppColors.Emerald500,
                                        modifier = Modifier.size(AppIcons.IconSizeMedium)
                                    )
                                }
                                isSelected && !isCorrect -> {
                                    Icon(
                                        imageVector = Icons.Rounded.Cancel,
                                        contentDescription = "Incorrect",
                                        tint = AppColors.Rose500,
                                        modifier = Modifier.size(AppIcons.IconSizeMedium)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (isEvaluated && data.selectedOptionId != null) {
                val selectedOption = multipleChoiceContent.options.find { it.id == data.selectedOptionId }
                val isSelectedCorrect = selectedOption?.isCorrect == true

                val feedbackBgColor = if (isSelectedCorrect) AppColors.Emerald50 else AppColors.Rose50
                val feedbackBorderColor = if (isSelectedCorrect) AppColors.Emerald500 else AppColors.Rose500
                val feedbackTextColor = if (isSelectedCorrect) AppColors.Emerald600 else AppColors.Rose600
                val feedbackIcon = if (isSelectedCorrect) Icons.Rounded.CheckCircle else Icons.Rounded.Cancel
                val feedbackMessage = if (isSelectedCorrect)
                    stringResource(R.string.noti_correct) else stringResource(R.string.noti_incorrect)

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = Spaces.Space8),
                    shape = RoundedCornerShape(AppRadius.RadiusLarge),
                    color = feedbackBgColor,
                    border = BorderStroke(Spaces.Space1, feedbackBorderColor)
                ) {
                    Row(
                        modifier = Modifier.padding(Spaces.Space16),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = feedbackIcon,
                            contentDescription = null,
                            tint = feedbackBorderColor,
                            modifier = Modifier.size(Spaces.Space28)
                        )
                        Spacer(modifier = Modifier.width(Spaces.Space12))
                        Text(
                            text = feedbackMessage,
                            color = feedbackTextColor,
                            fontSize = AppTextSizes.TextSizeMain,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}