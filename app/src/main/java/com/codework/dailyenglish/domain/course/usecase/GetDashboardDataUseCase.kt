package com.codework.dailyenglish.domain.course.usecase

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.course.model.CourseDashboardData
import com.codework.dailyenglish.domain.course.repository.CourseRepository
import javax.inject.Inject

class GetDashboardDataUseCase @Inject constructor(
    private val repository: CourseRepository
) {
    suspend operator fun invoke(): AppResult<CourseDashboardData> {
        return repository.getCourseDashboardData()
    }
}