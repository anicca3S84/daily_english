package com.codework.dailyenglish.domain.course_detail.repository

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.course_detail.model.CourseDetailData

interface CourseDetailRepository {
    suspend fun getCourseDetail(courseId: String): AppResult<CourseDetailData>
}