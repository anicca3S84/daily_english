package com.codework.dailyenglish.data.course_detail.repository

import com.codework.dailyenglish.core.dispatchers.Dispatcher
import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.map
import com.codework.dailyenglish.data.course_detail.mapper.toDomain
import com.codework.dailyenglish.data.course_detail.remote.datasource.CourseDetailRemoteDataSource
import com.codework.dailyenglish.domain.course_detail.model.CourseDetailData
import com.codework.dailyenglish.domain.course_detail.repository.CourseDetailRepository
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