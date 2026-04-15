package com.codework.dailyenglish.data.course_detail.remote

import com.codework.dailyenglish.data.course_detail.remote.dto.CourseDetailDto
import com.codework.dailyenglish.data.remote.response.BaseResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CourseDetailApi {
    @GET("courses/{courseId}/detail")
    suspend fun getCourseDetail(@Path("courseId") courseId: String): BaseResponse<CourseDetailDto>
}