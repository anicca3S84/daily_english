package com.codework.dailyenglish.domain.course.repository

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.course.model.CourseDashboardData

interface CourseRepository {
    suspend fun getCourseDashboardData(): AppResult<CourseDashboardData>
}