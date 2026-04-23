package com.codework.dailyenglish.data.lesson.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.data.lesson.remote.dto.LessonPlayResponseDto

interface LessonRemoteDataSource {
    suspend fun getLessonPlayData(lessonId: String): AppResult<LessonPlayResponseDto>
    suspend fun updateLessonProgress(
        lessonId: String,
        cursor: Int,
        questionId: String?,
        optionId: String?,
        isCorrect: Boolean?
    ): AppResult<Unit>
}