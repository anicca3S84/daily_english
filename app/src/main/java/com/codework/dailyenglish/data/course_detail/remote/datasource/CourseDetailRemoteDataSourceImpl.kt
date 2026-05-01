package com.codework.dailyenglish.data.course_detail.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.safeApiCall
import com.codework.dailyenglish.core.network.unwrap
import com.codework.dailyenglish.data.course_detail.remote.CourseDetailApi
import com.codework.dailyenglish.data.course_detail.remote.dto.CourseDetailDto
import javax.inject.Inject

class CourseDetailRemoteDataSourceImpl @Inject constructor(
    private val api: CourseDetailApi
) : CourseDetailRemoteDataSource {

    override suspend fun getCourseDetail(courseId: String): AppResult<CourseDetailDto> {
         return safeApiCall { api.getCourseDetail(courseId) }.unwrap()
    }
}