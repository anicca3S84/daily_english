package com.codework.dailyenglish.domain.lesson.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.lesson.model.LessonResult
import com.codework.dailyenglish.domain.lesson.repository.LessonRepository
import jakarta.inject.Inject

class CompleteLessonUseCase @Inject constructor(
    private val repository: LessonRepository
) {
    suspend operator fun invoke(lessonId: String): AppResult<LessonResult> {
        return repository.completeLesson(lessonId)
    }
}