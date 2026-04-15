package com.codework.dailyenglish.data.course_detail.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.data.course_detail.remote.dto.CourseDetailDto

interface CourseDetailRemoteDataSource {
    suspend fun getCourseDetail(courseId: String): AppResult<CourseDetailDto>
}