package com.codework.dailyenglish.presentation.screen.lessons.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.codework.dailyenglish.core.navigation.CollectEffect
import com.codework.dailyenglish.presentation.components.AppBottomSheet
import com.codework.dailyenglish.presentation.screen.lessons.preview.mvi.LessonPreviewEffect
import com.codework.dailyenglish.presentation.screen.lessons.preview.mvi.LessonPreviewEvent
import com.codework.dailyenglish.presentation.theme.CourseTheme

@Composable
fun LessonPreviewPopup(
    lessonId: String,
    visible: Boolean,
    isCompleted: Boolean,
    onDismiss: () -> Unit,
    onNavigateToLearn: (String) -> Unit,
    courseTheme: CourseTheme,
    viewModel: LessonPreviewViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = lessonId, key2 = visible) {
        if (visible && lessonId.isNotBlank()) {
            viewModel.onEvent(LessonPreviewEvent.OnLoadPreview(lessonId))
        }
    }

    CollectEffect(viewModel.effectFlow) { effect ->
        when (effect) {
            is LessonPreviewEffect.NavigateToLearningSession -> {
                onNavigateToLearn(effect.lessonId)
            }
            LessonPreviewEffect.CloseBottomSheet -> {
                onDismiss()
            }
        }
    }

    AppBottomSheet(
        visible = visible,
        onDismiss = onDismiss,
        enableDrag = true
    ) { animateClose ->
        LessonPreviewSheet(
            state = state,
            onEvent = viewModel::onEvent,
            theme = courseTheme,
            onClose = animateClose,
            isCompleted = isCompleted,
        )
    }
}