package com.codework.daily_english.data.course.remote.datasource

import com.codework.daily_english.core.result.AppResult
import com.codework.daily_english.data.course.remote.dto.CourseDashboardDto

interface CourseRemoteDataSource {
    suspend fun getCourseDashboardData(): AppResult<CourseDashboardDto>
}