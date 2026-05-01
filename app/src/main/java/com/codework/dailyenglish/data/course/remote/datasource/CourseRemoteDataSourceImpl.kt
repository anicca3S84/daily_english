package com.codework.dailyenglish.data.course.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.safeApiCall
import com.codework.dailyenglish.core.network.unwrap
import com.codework.dailyenglish.data.course.remote.CourseApi
import com.codework.dailyenglish.data.course.remote.dto.CourseDashboardDto
import jakarta.inject.Inject

class CourseRemoteDataSourceImpl @Inject constructor(
    private val api: CourseApi
) : CourseRemoteDataSource {

    override suspend fun getCourseDashboardData(): AppResult<CourseDashboardDto> {
        return safeApiCall { api.getCourseDashboardData() }.unwrap()
    }
}