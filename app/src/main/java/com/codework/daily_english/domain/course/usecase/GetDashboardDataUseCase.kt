package com.codework.daily_english.domain.course.usecase

import com.codework.daily_english.core.result.AppResult
import com.codework.daily_english.domain.course.model.CourseDashboardData
import com.codework.daily_english.domain.course.repository.CourseRepository
import javax.inject.Inject

class GetDashboardDataUseCase @Inject constructor(
    private val repository: CourseRepository
) {
    suspend operator fun invoke(): AppResult<CourseDashboardData> {
        return repository.getCourseDashboardData()
    }
}