package com.codework.daily_english.domain.course_detail.usecase

import com.codework.daily_english.core.network.AppResult
import com.codework.daily_english.domain.course_detail.model.CourseDetailData
import com.codework.daily_english.domain.course_detail.repository.CourseDetailRepository
import javax.inject.Inject

class GetCourseDetailUseCase @Inject constructor(
    private val repository: CourseDetailRepository
) {
    suspend operator fun invoke(courseId: String): AppResult<CourseDetailData> {
        return repository.getCourseDetail(courseId)
    }
}