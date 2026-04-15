package com.codework.dailyenglish.data.lesson_preview.remote

import com.codework.dailyenglish.data.lesson_preview.remote.dto.LessonPreviewDto
import com.codework.dailyenglish.data.remote.response.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface LessonPreviewApi {
    @GET("lessons/{lessonId}/preview")
    suspend fun getLessonPreview(@Path("lessonId") lessonId: String): BaseResponse<LessonPreviewDto>
}