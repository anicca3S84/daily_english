package com.codework.dailyenglish.domain.lesson.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.lesson.model.LessonPlay
import com.codework.dailyenglish.domain.lesson.repository.LessonRepository
import javax.inject.Inject

class GetLessonPlayDataUseCase @Inject constructor(
    private val repository: LessonRepository
) {
    suspend operator fun invoke(lessonId: String): AppResult<LessonPlay> {
        return repository.getLessonPlayData(lessonId)
    }
}