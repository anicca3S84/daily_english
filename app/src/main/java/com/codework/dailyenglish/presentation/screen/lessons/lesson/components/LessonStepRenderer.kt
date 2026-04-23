package com.codework.dailyenglish.presentation.screen.lessons.lesson.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.codework.dailyenglish.domain.lesson.model.LessonStep
import com.codework.dailyenglish.domain.lesson.model.StepData

@Composable
fun LessonStepRenderer(
    step: LessonStep,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (val stepData = step.data) {
        is StepData.Vocab -> {
            VocabCard(data = stepData, modifier = modifier)
        }
        is StepData.Question -> {
            QuestionCard(data = stepData, onOptionSelected = onOptionSelected, modifier = modifier)
        }
        is StepData.Theory -> {
            // TODO:
        }
        is StepData.Unknown -> {
        }
    }
}