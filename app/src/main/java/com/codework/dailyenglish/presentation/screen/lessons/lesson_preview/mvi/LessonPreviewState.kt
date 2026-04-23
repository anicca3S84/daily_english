package com.codework.dailyenglish.presentation.screen.lessons.lesson_preview.mvi

import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.domain.lesson_preview.model.LessonPreview

data class LessonPreviewState(
    val previewDataState: BaseUiState<LessonPreview> = BaseUiState()
)