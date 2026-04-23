package com.codework.dailyenglish.presentation.screen.lessons.lesson

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.codework.dailyenglish.domain.lesson.model.StepData
import com.codework.dailyenglish.presentation.components.GridBackground
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.LessonStepRenderer
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonEvent
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonState
import com.codework.dailyenglish.presentation.components.AppProgressTopBar
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.LessonBottomBar

@Composable
fun LessonScreen(
    state: LessonState,
    onEvent: (LessonEvent) -> Unit
) {
    val snackBar = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBar) },
        containerColor = Color.Transparent
    ) { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            GridBackground()

            StateWrapper(
                uiState = state,
                modifier = Modifier.fillMaxSize(),
                snackbarHostState = snackBar,
                onRetry = { /* TODO: Bạn có thể truyền event Reload vào đây */ }
            ) { config ->
                val data = config ?: return@StateWrapper

                Column(modifier = Modifier.fillMaxSize()) {
                    AppProgressTopBar(
                        progress = data.progressPercent,
                        progressText = data.progressText,
                        onBackClick = { onEvent(LessonEvent.OnQuitClicked) },
                        onCloseClick = { onEvent(LessonEvent.OnQuitClicked) }
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
                                onOptionSelected = { optionId ->
                                    onEvent(LessonEvent.OnOptionSelected(optionId))
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Box(modifier = Modifier.fillMaxSize())
                        }
                    }

                    LessonBottomBar(
                        currentStep = data.currentStep,
                        onBackClick = { onEvent(LessonEvent.OnPreviousStep) },
                        onNextClick = {
                            val stepData = data.currentStep?.data
                            if (stepData is StepData.Question && !stepData.isChecked) {
                                onEvent(LessonEvent.OnCheckAnswerClicked)
                            } else {
                                onEvent(LessonEvent.OnNextStep)
                            }
                        }
                    )
                }
            }
        }
    }
}