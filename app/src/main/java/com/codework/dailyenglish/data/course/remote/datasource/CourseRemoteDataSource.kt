package com.codework.dailyenglish.data.course.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.data.course.remote.dto.CourseDashboardDto

interface CourseRemoteDataSource {
    suspend fun getCourseDashboardData(): AppResult<CourseDashboardDto>
}