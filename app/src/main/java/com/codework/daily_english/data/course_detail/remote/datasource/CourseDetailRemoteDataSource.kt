package com.codework.daily_english.data.course_detail.remote.datasource

import com.codework.daily_english.core.network.AppResult
import com.codework.daily_english.data.course_detail.remote.dto.CourseDetailDto

interface CourseDetailRemoteDataSource {
    suspend fun getCourseDetail(courseId: String): AppResult<CourseDetailDto>
}