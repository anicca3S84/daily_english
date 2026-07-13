package com.codework.dailyenglish.presentation.screen.lessons.lesson

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    val data = state.data
    val context = LocalContext.current

    Scaffold(
        snackbarHost = { SnackbarHost(snackBar) },
        containerColor = AppColors.AppBackground,
        topBar = {
            if (data != null) {
                AppProgressTopBar(
                    progress = data.progressPercent,
                    progressText = data.progressText,
                    onBackClick = { onEvent(LessonEvent.OnPreviousStep) },
                    onCloseClick = { onEvent(LessonEvent.OnQuitClick) }
                )
            }
        },
        bottomBar = {
            if (data != null) {
                val isLastStep = data.currentIndex == data.steps.size - 1

                LessonBottomBar(
                    currentStep = data.currentStep,
                    isLastStep = isLastStep,
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
                        .padding(horizontal = Spaces.Space16)
                        .padding(bottom = Spaces.Space16)
                )
            }
        }
    ) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {
            StateWrapper(
                uiState = state,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                snackbarHostState = snackBar,
                onRetry = { /* TODO */ }
            ) { config ->
                val validData = config ?: return@StateWrapper

                AnimatedContent(
                    targetState = validData.currentIndex,
                    modifier = Modifier.fillMaxSize(),
                    label = "LessonStepAnimation"
                ) { index ->
                    val step = validData.steps.getOrNull(index)

                    if (step != null) {
                        LessonStepRenderer(
                            step = step,
                            voiceState = validData.voiceState,
                            onAnswerInput = { answer, indices ->
                                onEvent(LessonEvent.OnAnswerInput(answer = answer, indices = indices))
                            },
                            onStartSpeech = { onEvent(LessonEvent.OnStartSpeech(context)) },
                            onStopSpeech = { onEvent(LessonEvent.OnStopSpeech) },
                            onPlayAudio = { url, word, speed ->
                                onEvent(LessonEvent.OnPlayAudio(url, word, speed))
                            },
                            onToggleSaveWord = { wordId ->
                                onEvent(LessonEvent.OnToggleSaveWord(wordId))
                            },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }

            if (data != null) {
                val currentStepData = data.currentStep?.data as? StepData.Question
                val shouldShowSheet = data.showResultStepIndex == data.currentIndex
                val isCorrect = currentStepData?.isCorrect == true

                val isLastStep = data.currentIndex == data.steps.lastIndex

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
                        isLastStep = isLastStep,
                        correctAnswer = correctAnswerText,
                        onNextClicked = { onEvent(LessonEvent.OnNextStep) }
                    )
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