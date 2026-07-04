package com.codework.dailyenglish.data.lesson

import com.codework.dailyenglish.data.lesson.remote.dto.LessonPlayResponseDto
import com.codework.dailyenglish.data.lesson.remote.dto.LessonResultResponseDto
import com.codework.dailyenglish.data.lesson.remote.dto.UpdateProgressRequestDto
import com.codework.dailyenglish.data.remote.response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface LessonApi {
    @GET("lessons/{lessonId}/play")
    suspend fun getLessonPlayData(
        @Path("lessonId") lessonId: String,
    ): BaseResponse<LessonPlayResponseDto>

    @POST("lessons/{lessonId}/restart_lesson")
    suspend fun restartLesson(
        @Path("lessonId") lessonId: String,
    ): BaseResponse<LessonPlayResponseDto>

    @PUT("lessons/{lessonId}/progress")
    suspend fun updateLessonProgress(
        @Path("lessonId") lessonId: String,
        @Body request: UpdateProgressRequestDto
    ): BaseResponse<Any>

    @POST("lessons/{lessonPublicId}/complete")
    suspend fun completeLesson(
        @Path("lessonPublicId") lessonId: String
    ): BaseResponse<LessonResultResponseDto>

    @POST("lessons/{lessonId}/review-lesson")
    suspend fun reviewLesson(
        @Path("lessonId") lessonId: String
    ): BaseResponse<LessonPlayResponseDto>
}