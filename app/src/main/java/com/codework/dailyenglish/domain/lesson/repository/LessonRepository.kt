package com.codework.dailyenglish.domain.lesson.repository

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.data.lesson.remote.dto.LessonResultResponseDto
import com.codework.dailyenglish.domain.lesson.model.LessonPlay
import com.codework.dailyenglish.domain.lesson.model.LessonResult

interface LessonRepository {
    suspend fun getLessonPlayData(lessonId: String): AppResult<LessonPlay>
    suspend fun updateLessonProgress(
        lessonId: String,
        cursor: Int,
        questionId: String?,
        optionId: String?,
        userAnswer: String?,
        selectedIndices: List<Int>?,
        isCorrect: Boolean?
    ): AppResult<Unit>

    suspend fun completeLesson(lessonPublicId: String): AppResult<LessonResult>}