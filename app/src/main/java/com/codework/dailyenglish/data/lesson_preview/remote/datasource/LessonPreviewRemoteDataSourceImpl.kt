package com.codework.dailyenglish.data.lesson_preview.remote.datasource
import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.safeApiCall
import com.codework.dailyenglish.core.network.unwrap
import com.codework.dailyenglish.data.lesson_preview.remote.dto.LessonPreviewDto
import com.codework.dailyenglish.data.lesson_preview.remote.LessonPreviewApi
import jakarta.inject.Inject

class LessonPreviewRemoteDataSourceImpl @Inject constructor(
    private val api: LessonPreviewApi
) : LessonPreviewRemoteDataSource {
    override suspend fun getLessonPreview(lessonId: String): AppResult<LessonPreviewDto> {
        return safeApiCall { api.getLessonPreview(lessonId) }.unwrap()
    }
}