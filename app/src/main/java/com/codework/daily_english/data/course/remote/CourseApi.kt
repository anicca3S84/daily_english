package com.codework.daily_english.data.course.remote

import com.codework.daily_english.data.course.remote.dto.CourseDashboardDto
import com.codework.daily_english.data.remote.response.BaseResponse
import retrofit2.http.GET

interface CourseApi {
    @GET("courses/dashboard")
    suspend fun getCourseDashboardData(): BaseResponse<CourseDashboardDto>
}