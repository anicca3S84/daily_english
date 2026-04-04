package com.codework.daily_english.data.course_detail.repository

import com.codework.daily_english.core.dispatchers.Dispatcher
import com.codework.daily_english.core.result.AppResult
import com.codework.daily_english.core.result.map
import com.codework.daily_english.data.course_detail.mapper.toDomain
import com.codework.daily_english.data.course_detail.remote.datasource.CourseDetailRemoteDataSource
import com.codework.daily_english.domain.course_detail.model.CourseDetailData
import com.codework.daily_english.domain.course_detail.repository.CourseDetailRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CourseDetailRepositoryImpl @Inject constructor(
    private val remoteDataSource: CourseDetailRemoteDataSource,
    private val dispatchers: Dispatcher
) : CourseDetailRepository {

    override suspend fun getCourseDetail(courseId: String): AppResult<CourseDetailData> =
        withContext(dispatchers.io) {

            val responseResult = remoteDataSource.getCourseDetail(courseId)

            responseResult.map { dto ->
                dto.toDomain()
            }
        }
}