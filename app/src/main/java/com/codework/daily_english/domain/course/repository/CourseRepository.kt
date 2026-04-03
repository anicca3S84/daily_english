package com.codework.daily_english.domain.course.repository

import com.codework.daily_english.core.result.AppResult
import com.codework.daily_english.domain.course.model.CourseDashboardData

interface CourseRepository {
    suspend fun getCourseDashboardData(): AppResult<CourseDashboardData>
}