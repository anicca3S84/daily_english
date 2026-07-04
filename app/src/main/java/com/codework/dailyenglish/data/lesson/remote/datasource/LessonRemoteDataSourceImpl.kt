package com.codework.dailyenglish.data.lesson.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.safeApiCall
import com.codework.dailyenglish.core.network.unwrap
import com.codework.dailyenglish.core.network.map
import com.codework.dailyenglish.data.lesson.LessonApi
import com.codework.dailyenglish.data.lesson.remote.dto.LessonPlayResponseDto
import com.codework.dailyenglish.data.lesson.remote.dto.LessonResultResponseDto
import com.codework.dailyenglish.data.lesson.remote.dto.UpdateProgressRequestDto
import javax.inject.Inject

class LessonRemoteDataSourceImpl @Inject constructor(
    private val api: LessonApi
) : LessonRemoteDataSource {

    override suspend fun getLessonPlayData(lessonId: String): AppResult<LessonPlayResponseDto> {
        return safeApiCall {
            api.getLessonPlayData(lessonId)
        }.unwrap()
    }

    override suspend fun restartLesson(lessonId: String): AppResult<LessonPlayResponseDto> {
        return safeApiCall {
            api.restartLesson(lessonId)
        }.unwrap()
    }

    override suspend fun updateLessonProgress(
        lessonId: String,
        cursor: Int,
        questionId: String?,
        optionId: String?,
        userAnswer: String?,
        selectedIndices: List<Int>?,
        isCorrect: Boolean?
    ): AppResult<Unit> {
        return safeApiCall {
            val request = UpdateProgressRequestDto(
                resumeCursor = cursor,
                questionId = questionId,
                selectedOptionId = optionId,
                isCorrect = isCorrect,
                userAnswer = userAnswer,
                selectedIndices = selectedIndices
            )
            api.updateLessonProgress(lessonId, request)
        }.unwrap().map { Unit }
    }

    override suspend fun completeLesson(lessonPublicId: String): AppResult<LessonResultResponseDto> {
        return safeApiCall {
            api.completeLesson(lessonPublicId)
        }.unwrap()
    }

    override suspend fun reviewLesson(lessonId: String): AppResult<LessonPlayResponseDto> {
        return safeApiCall {
            api.reviewLesson(lessonId)
        }.unwrap()
    }
}