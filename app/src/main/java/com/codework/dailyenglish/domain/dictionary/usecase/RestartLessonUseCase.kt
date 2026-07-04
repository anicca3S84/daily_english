package com.codework.dailyenglish.domain.dictionary.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.lesson.model.LessonPlay
import com.codework.dailyenglish.domain.lesson.repository.LessonRepository
import javax.inject.Inject


class RestartLessonUseCase @Inject constructor(
    private val repository: LessonRepository
) {
    suspend operator fun invoke(lessonId: String): AppResult<LessonPlay> {
        return repository.restartLesson(lessonId)
    }
}