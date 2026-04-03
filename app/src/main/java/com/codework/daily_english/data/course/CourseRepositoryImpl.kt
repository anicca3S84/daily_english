package com.codework.daily_english.data.course

import com.codework.daily_english.core.dispatchers.Dispatcher
import com.codework.daily_english.core.result.AppResult
import com.codework.daily_english.core.result.map
import com.codework.daily_english.data.course.remote.CourseRemoteDataSource
import com.codework.daily_english.domain.course.model.CourseDashboardData
import com.codework.daily_english.domain.course.repository.CourseRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CourseRepositoryImpl @Inject constructor(
    private val remoteDataSource: CourseRemoteDataSource,
    private val dispatchers: Dispatcher
) : CourseRepository {

    override suspend fun getCourseDashboardData(): AppResult<CourseDashboardData> = withContext(dispatchers.io) {
        val responseResult = remoteDataSource.getCourseDashboardData()
        responseResult.map { dto ->
            dto.toDomain()
        }
    }
}