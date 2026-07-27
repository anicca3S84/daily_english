package com.codework.dailyenglish.presentation.screen.lessons.lesson.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.codework.dailyenglish.core.utils.VoiceToTextParserState
import com.codework.dailyenglish.domain.dictionary.model.Word
import com.codework.dailyenglish.domain.lesson.model.LessonStep
import com.codework.dailyenglish.domain.lesson.model.QuestionContent
import com.codework.dailyenglish.domain.lesson.model.StepData
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.FillBlankQuestion
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.MultipleChoiceQuestion
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.SentenceBuilderQuestion
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.question.SpeakingQuestion
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.vocab.VocabCard
import com.codework.dailyenglish.presentation.utils.calculateSimilarity

@Composable
fun LessonStepRenderer(
    modifier: Modifier = Modifier,
    step: LessonStep,
    voiceState: VoiceToTextParserState,
    onAnswerInput: (String, List<Int>) -> Unit,
    onStartSpeech: () -> Unit,
    onStopSpeech: () -> Unit,
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
                        onPlayAudio = onPlayAudio,
                        onAnswerSelected = { answer ->
                            onAnswerInput(answer, emptyList())
                        },
                        isCorrect = stepData.isCorrect,

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
                    val answerText = stepData.userAnswer ?: ""
                    val scoreVal = if (stepData.isAnswerEvaluated && answerText.isNotBlank()) {
                        if (answerText == "PASSED") {
                            100
                        } else {
                            (answerText.calculateSimilarity(content.targetText) * 100).toInt().coerceIn(0, 100)
                        }
                    } else null

                    SpeakingQuestion(
                        prompt = content.prompt,
                        targetText = content.targetText,
                        phonetic = content.phonetic,
                        translation = content.translation,
                        initialAnswer = stepData.userAnswer,
                        isEvaluated = stepData.isAnswerEvaluated,
                        isCorrect = stepData.isCorrect,
                        score = scoreVal,
                        voiceState = voiceState,
                        onStartSpeech = onStartSpeech,
                        onStopSpeech = onStopSpeech,
                        onAnswerSelected = { text ->
                            onAnswerInput(text, emptyList())
                        },
                        onPlayAudio = {
                            onPlayAudio(null, content.targetText, 1.0f)
                        },
                        onPlayAudioSlow = {
                            onPlayAudio(null, content.targetText, 0.5f)
                        },
                        modifier = modifier
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