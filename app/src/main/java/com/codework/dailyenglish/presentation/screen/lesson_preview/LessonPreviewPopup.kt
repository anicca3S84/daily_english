package com.codework.dailyenglish.presentation.screen.lesson_preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.codework.dailyenglish.core.navigation.CollectEffect
import com.codework.dailyenglish.presentation.components.AppBottomSheet
import com.codework.dailyenglish.presentation.screen.lesson_preview.mvi.LessonPreviewEffect
import com.codework.dailyenglish.presentation.screen.lesson_preview.mvi.LessonPreviewEvent
import com.codework.dailyenglish.presentation.theme.CourseTheme

@Composable
fun LessonPreviewPopup(
    lessonId: String,
    visible: Boolean,
    onDismiss: () -> Unit,
    onNavigateToLearn: (String) -> Unit,
    courseTheme: CourseTheme,
    viewModel: LessonPreviewViewModel = hiltViewModel()
) {
    // 🔥 THÊM DÒNG NÀY ĐỂ FIX LỖI "Unresolved reference: state"
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = lessonId, key2 = visible) {
        if (visible && lessonId.isNotBlank()) {
            viewModel.onEvent(LessonPreviewEvent.OnLoadPreview(lessonId))
        }
    }

    // Lắng nghe các Effect từ ViewModel
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

    // Wrap nội dung bằng AppBottomSheet custom
    AppBottomSheet(
        visible = visible,
        onDismiss = onDismiss,
        maxHeight = 0.68f,
        enableDrag = true
    ) {
        // Bây giờ 'state' đã tồn tại và có thể truyền vào đây
        LessonPreviewSheet(
            state = state,
            onEvent = viewModel::onEvent,
            theme = courseTheme,
        )
    }
}