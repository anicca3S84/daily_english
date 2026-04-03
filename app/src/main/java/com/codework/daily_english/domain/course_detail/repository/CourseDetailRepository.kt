package com.codework.daily_english.domain.course_detail.repository

import com.codework.daily_english.core.result.AppResult
import com.codework.daily_english.domain.course_detail.model.CourseDetailData

interface CourseDetailRepository {
    suspend fun getCourseDetail(courseId: String): AppResult<CourseDetailData>
}