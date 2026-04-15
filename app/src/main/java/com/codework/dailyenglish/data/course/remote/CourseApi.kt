package com.codework.dailyenglish.data.course.remote

import com.codework.dailyenglish.data.course.remote.dto.CourseDashboardDto
import com.codework.dailyenglish.data.remote.response.BaseResponse
import retrofit2.http.GET

interface CourseApi {
    @GET("courses/dashboard")
    suspend fun getCourseDashboardData(): BaseResponse<CourseDashboardDto>
}