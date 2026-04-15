package com.codework.dailyenglish.domain.lesson_preview.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.lesson_preview.model.LessonPreview
import com.codework.dailyenglish.domain.lesson_preview.repository.LessonPreviewRepository
import javax.inject.Inject

class GetLessonPreviewUseCase @Inject constructor(
    private val repository: LessonPreviewRepository
) {
    suspend operator fun invoke(lessonId: String): AppResult<LessonPreview> {
        return repository.getLessonPreview(lessonId)
    }
}