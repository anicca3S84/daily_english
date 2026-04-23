package com.codework.dailyenglish.domain.lesson.repository

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.lesson.model.LessonPlay

interface LessonRepository {
    suspend fun getLessonPlayData(lessonId: String): AppResult<LessonPlay>
    suspend fun updateLessonProgress(
        lessonId: String,
        cursor: Int,
        questionId: String?,
        optionId: String?,
        isCorrect: Boolean?
    ): AppResult<Unit>
}