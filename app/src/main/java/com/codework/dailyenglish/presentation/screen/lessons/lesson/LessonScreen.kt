package com.codework.dailyenglish.presentation.screen.lessons.lesson

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.codework.dailyenglish.domain.lesson.model.StepData
import com.codework.dailyenglish.presentation.components.AppConfirmDialog
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.LessonStepRenderer
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonEvent
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonState
import com.codework.dailyenglish.presentation.components.AppProgressTopBar
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.LessonBottomBar
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.Spaces
import com.codework.dailyenglish.R
import com.codework.dailyenglish.domain.lesson.model.QuestionContent
import com.codework.dailyenglish.presentation.components.AnswerBottomSheet

@Composable
fun LessonScreen(
    state: LessonState,
    onEvent: (LessonEvent) -> Unit
) {
    val snackBar = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBar) },
        containerColor = AppColors.AppBackground
    ) { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            StateWrapper(
                uiState = state,
                modifier = Modifier.fillMaxSize(),
                snackbarHostState = snackBar,
                onRetry = { /* TODO */ }
            ) { config ->
                val data = config ?: return@StateWrapper

                Box(modifier = Modifier.fillMaxSize()) {

                    Column(modifier = Modifier.fillMaxSize()) {
                        AppProgressTopBar(
                            progress = data.progressPercent,
                            progressText = data.progressText,
                            onBackClick = { onEvent(LessonEvent.OnPreviousStep) },
                            onCloseClick = { onEvent(LessonEvent.OnQuitClick) }
                        )

                        AnimatedContent(
                            targetState = data.currentIndex,
                            modifier = Modifier.weight(1f),
                            transitionSpec = {
                                (slideInHorizontally { it } + fadeIn()).togetherWith(
                                    slideOutHorizontally { -it } + fadeOut()
                                )
                            },
                            label = "LessonStepAnimation"
                        ) { index ->
                            val step = data.steps.getOrNull(index)

                            if (step != null) {
                                LessonStepRenderer(
                                    step = step,
                                    onAnswerInput = { answer, indices ->
                                        onEvent(LessonEvent.OnAnswerInput(answer = answer, indices = indices))
                                    },
                                    onPlayAudio = { url, word, speed ->
                                        onEvent(LessonEvent.OnPlayAudio(url, word, speed))
                                    },
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }
                        }
                    }
                    val currentStepData = data.currentStep?.data as? StepData.Question

                    val shouldShowSheet = data.showResultStepIndex == data.currentIndex
                    val isCorrect = currentStepData?.isCorrect == true

                    LessonBottomBar(
                        currentStep = data.currentStep,
                        onNextClick = {
                            val stepData = data.currentStep?.data
                            if (stepData is StepData.Question) {
                                if (stepData.isAnswerEvaluated) {
                                    onEvent(LessonEvent.OnNextStep)
                                } else {
                                    if (stepData.isUserAnswered) {
                                        onEvent(LessonEvent.OnCheckAnswerClicked)
                                    }
                                }
                            } else {
                                onEvent(LessonEvent.OnNextStep)
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = Spaces.Space16)
                    )

                    if (shouldShowSheet && currentStepData != null) {
                        val correctAnswerText = when (val content = currentStepData.content) {
                            is QuestionContent.MultipleChoice -> content.options.find { it.isCorrect }?.text
                            is QuestionContent.SentenceBuilder -> content.correctSentence
                            is QuestionContent.FillBlank -> content.correctAnswer
                            is QuestionContent.Dictation -> content.correctAnswer
                            is QuestionContent.Speaking -> content.targetText
                            else -> null
                        }
                        AnswerBottomSheet(
                            isCorrect = isCorrect,
                            correctAnswer = correctAnswerText,
                            onNextClicked = { onEvent(LessonEvent.OnNextStep) }
                        )
                    }
                }
            }
        }

        if (state.data?.isQuitDialogVisible == true) {
            AppConfirmDialog(
                title = stringResource(R.string.msg_exit_lesson),
                message = stringResource(R.string.msg_quit_lesson),
                confirmText = stringResource(R.string.label_exit),
                cancelText = stringResource(R.string.label_cancel),
                onConfirm = {
                    onEvent(LessonEvent.OnConfirmQuit)
                },
                onDismiss = {
                    onEvent(LessonEvent.OnDismissQuitDialog)
                }
            )
        }
    }
}