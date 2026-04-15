package com.codework.dailyenglish.data.course.repository

import com.codework.dailyenglish.core.dispatchers.Dispatcher
import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.map
import com.codework.dailyenglish.data.course.mapper.toDomain
import com.codework.dailyenglish.data.course.remote.datasource.CourseRemoteDataSource
import com.codework.dailyenglish.domain.course.model.CourseDashboardData
import com.codework.dailyenglish.domain.course.repository.CourseRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CourseRepositoryImpl @Inject constructor(
    private val remoteDataSource: CourseRemoteDataSource,
    private val dispatchers: Dispatcher
) : CourseRepository {

    override suspend fun getCourseDashboardData(): AppResult<CourseDashboardData> =
        withContext(dispatchers.io) {
            val responseResult = remoteDataSource.getCourseDashboardData()
            responseResult.map { dto ->
                dto.toDomain()
            }
        }
}