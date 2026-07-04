package com.codework.dailyenglish.presentation.screen.lessons.lesson.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.codework.dailyenglish.domain.dictionary.model.Word
import com.codework.dailyenglish.domain.lesson.model.LessonStep
import com.codework.dailyenglish.domain.lesson.model.QuestionContent
import com.codework.dailyenglish.domain.lesson.model.StepData
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.FillBlankQuestion
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.MultipleChoiceQuestion
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.SentenceBuilderQuestion
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.SpeakingQuestion
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.vocab.VocabCard

@Composable
fun LessonStepRenderer(
    modifier: Modifier = Modifier,
    step: LessonStep,
    onAnswerInput: (String, List<Int>) -> Unit,
    onPlayAudio: (String?, String, Float) -> Unit = { _, _, _ -> },
    onToggleSaveWord: (String) -> Unit = {}
) {
    when (val stepData = step.data) {
        is StepData.Vocab -> {
            VocabCard(
                data = stepData,
                modifier = modifier,
                isFavorite = stepData.isSaved ?: false,
                onToggleFavorite = { onToggleSaveWord(stepData.id) },
                onPlayAudio = onPlayAudio
            )
        }

        is StepData.Question -> {
            when (val content = stepData.content) {
                is QuestionContent.MultipleChoice -> {
                    MultipleChoiceQuestion(
                        data = stepData,
                        onPlayAudio = onPlayAudio,
                        onOptionSelected = { optionId ->
                            onAnswerInput(optionId, emptyList())
                        },
                        modifier = modifier
                    )
                }

                is QuestionContent.FillBlank -> {
                    FillBlankQuestion(
                        prompt = content.prompt,
                        sentenceWithBlank = content.sentenceWithBlank,
                        hintMask = content.hintMask,
                        initialAnswer = stepData.userAnswer,
                        onAnswerSelected = { answer ->
                            onAnswerInput(answer, emptyList())
                        }
                    )
                }

                is QuestionContent.SentenceBuilder -> {
                    SentenceBuilderQuestion(
                        prompt = content.prompt,
                        words = content.words,
                        correctSentence = content.correctSentence,
                        selectedIndices = stepData.selectedIndices,
                        isEvaluated = stepData.isAnswerEvaluated,
                        isCorrect = stepData.isCorrect,
                        onAnswerSelected = { indices, text ->
                            onAnswerInput(text, indices)
                        },
                        onPlayAudio = onPlayAudio
                    )
                }

                is QuestionContent.Speaking -> {
                    SpeakingQuestion(
                        prompt = content.prompt,
                        targetText = content.targetText,
                        phonetic = content.phonetic,
                        translation = content.translation,
                        initialAnswer = stepData.userAnswer,
                        isEvaluated = stepData.isAnswerEvaluated,
                        isCorrect = stepData.isCorrect,
                        onAnswerSelected = { text ->
                            onAnswerInput(text, emptyList())
                        },
                        onPlayAudio = {
                            onPlayAudio(null, content.targetText, 1.0f)
                        }
                    )
                }

                is QuestionContent.Dictation -> {
                    // TODO: UI cho Dictation
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