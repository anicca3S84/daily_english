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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.codework.dailyenglish.domain.lesson.model.StepData
import com.codework.dailyenglish.presentation.components.GridBackground
import com.codework.dailyenglish.presentation.components.StateWrapper
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.LessonStepRenderer
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonEvent
import com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi.LessonState
import com.codework.dailyenglish.presentation.components.AppProgressTopBar
import com.codework.dailyenglish.presentation.screen.lessons.lesson.components.LessonBottomBar
import com.codework.dailyenglish.presentation.theme.AppColors
import com.codework.dailyenglish.presentation.theme.Spaces

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
                            }
                        }
                    }

                    LessonBottomBar(
                        currentStep = data.currentStep,
                        onNextClick = {
                            val stepData = data.currentStep?.data
                            if (stepData is StepData.Question) {
                                if (stepData.isAnswerEvaluated) {
                                    onEvent(LessonEvent.OnNextStep)
                                } else {
                                    if (stepData.selectedOptionId != null) {
                                        onEvent(LessonEvent.OnCheckAnswerClicked)
                                    }
                                }
                            } else {
                                onEvent(LessonEvent.OnNextStep)
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = Spaces.Space16, vertical = Spaces.Space38)
                    )
                }
            }
        }
    }
}