package com.codework.dailyenglish.domain.lesson.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.lesson.repository.LessonRepository
import javax.inject.Inject

class UpdateLessonProgressUseCase @Inject constructor(
    private val repository: LessonRepository
) {
    suspend operator fun invoke(
        lessonId: String,
        cursor: Int,
        questionId: String? = null,
        optionId: String? = null,
        userAnswer: String? = null,
        selectedIndices: List<Int>? = null,
        isCorrect: Boolean? = null
    ): AppResult<Unit> {
        return repository.updateLessonProgress(
            lessonId, cursor, questionId, optionId, userAnswer, selectedIndices, isCorrect
        )
    }
}