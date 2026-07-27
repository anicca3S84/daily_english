package com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.dailyenglish.domain.lesson.model.QuestionContent
import com.codework.dailyenglish.domain.lesson.model.StepData
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.components.AudioWaveform
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.components.FlatQuizOption
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.components.PlayButton3D
import com.codework.dailyenglish.presentation.theme.AppColors

private val ColorTextMain = Color(0xFF1E293B)
private val ColorTextSub = Color(0xFF64748B)



@Composable
fun MultipleChoiceQuestion(
    data: StepData.Question,
    onOptionSelected: (String) -> Unit,
    onPlayAudio: (String?, String, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val content = data.content as? QuestionContent.MultipleChoice ?: return
    val isEvaluated = data.isAnswerEvaluated
    val textToRead = content.audioUrl ?: content.prompt
    val hasAudio = !content.audioUrl.isNullOrEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppColors.AppBackground)
            .padding(horizontal = 20.dp, vertical = 24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Choose the correct answer",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = ColorTextMain
        )

        Column(modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)) {
            Text(
                text = content.prompt,
                fontSize = 18.sp,
                fontWeight = FontWeight.W400,
                color = ColorTextSub,
            )
        }

        if (hasAudio) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = BorderStroke(2.dp, Color(0xFFEFF1F4))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    PlayButton3D(onClick = { onPlayAudio(null, textToRead, 1f) })
                    AudioWaveform(
                        modifier = Modifier
                            .weight(1f)
                            .height(36.dp),
                        color = Color(0xFF3B82F6)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            content.options.forEachIndexed { index, option ->
                val letter = when (index) {
                    0 -> "A"
                    1 -> "B"
                    2 -> "C"
                    3 -> "D"
                    else -> ('A' + index).toString()
                }

                FlatQuizOption(
                    text = option.text,
                    letter = letter,
                    optionId = option.id,
                    selectedOptionId = data.selectedOptionId,
                    isCorrect = option.isCorrect,
                    isEvaluated = isEvaluated,
                    onClick = onOptionSelected
                )
            }
        }
    }
}