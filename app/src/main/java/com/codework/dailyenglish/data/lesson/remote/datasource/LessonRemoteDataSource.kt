package com.codework.dailyenglish.data.lesson.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.data.lesson.remote.dto.LessonPlayResponseDto
import com.codework.dailyenglish.data.lesson.remote.dto.LessonResultResponseDto

interface LessonRemoteDataSource {
    suspend fun getLessonPlayData(lessonId: String): AppResult<LessonPlayResponseDto>
    suspend fun restartLesson(lessonId: String): AppResult<LessonPlayResponseDto>
    suspend fun reviewLesson(lessonId: String): AppResult<LessonPlayResponseDto>
    suspend fun updateLessonProgress(
        lessonId: String,
        cursor: Int,
        questionId: String?,
        optionId: String?,
        userAnswer: String?,
        selectedIndices: List<Int>?,
        isCorrect: Boolean?
    ): AppResult<Unit>
    suspend fun completeLesson(lessonPublicId: String): AppResult<LessonResultResponseDto>
}