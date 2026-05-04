package com.codework.dailyenglish.presentation.screen.lessons.lesson.mvi

import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.domain.lesson.model.LessonStep
import kotlin.collections.getOrNull

data class LessonConfigUi(
    val lessonId: String,
    val steps: List<LessonStep>,
    val currentIndex: Int,
    val isQuitDialogVisible: Boolean = false,
    val showResultStepIndex: Int? = null
) {
    val currentStep: LessonStep?
        get() = steps.getOrNull(currentIndex)

    val progressPercent: Float
        get() = if (steps.isEmpty()) 0f else (currentIndex + 1).toFloat() / steps.size

    val progressText: String
        get() = "${currentIndex + 1} / ${steps.size}"
}

typealias LessonState = BaseUiState<LessonConfigUi>