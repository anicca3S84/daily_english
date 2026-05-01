package com.codework.dailyenglish.presentation.screen.lessons.lesson.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.codework.dailyenglish.domain.lesson.model.LessonStep
import com.codework.dailyenglish.domain.lesson.model.QuestionContent
import com.codework.dailyenglish.domain.lesson.model.StepData
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.FillBlankQuestion
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.MultipleChoiceQuestion
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.vocab.VocabCard

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
            when (val content = stepData.content) {
                is QuestionContent.MultipleChoice -> {
                    MultipleChoiceQuestion(
                        data = stepData,
                        onOptionSelected = onOptionSelected,
                        modifier = modifier
                    )
                }

                is QuestionContent.FillBlank -> {
                    FillBlankQuestion(
                        prompt = content.prompt,
                        sentenceWithBlank = content.sentenceWithBlank,
                        options = content.options,
                        initialAnswer = stepData.selectedOptionId,
                        onAnswerSelected = onOptionSelected
                    )
                }

                is QuestionContent.SentenceBuilder -> {
                    // TODO: UI cho SentenceBuilder
                }

                is QuestionContent.Dictation -> {
                    // TODO: UI cho Dictation
                }

                is QuestionContent.Speaking -> {
                    // TODO: UI cho Speaking
                }

                is QuestionContent.Matching -> {
                    // TODO: UI cho Matching
                }

                is QuestionContent.Unknown -> {
                }
            }
        }

        is StepData.Theory -> {
        }

        is StepData.Unknown -> {
        }
    }
}