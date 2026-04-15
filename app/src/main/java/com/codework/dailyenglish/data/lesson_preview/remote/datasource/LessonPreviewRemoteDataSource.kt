package com.codework.dailyenglish.data.lesson_preview.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.data.lesson_preview.remote.dto.LessonPreviewDto

interface LessonPreviewRemoteDataSource {
    suspend fun getLessonPreview(lessonId: String): AppResult<LessonPreviewDto>
}