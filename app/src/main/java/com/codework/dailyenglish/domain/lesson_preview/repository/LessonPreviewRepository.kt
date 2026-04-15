package com.codework.dailyenglish.domain.lesson_preview.repository

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.domain.lesson_preview.model.LessonPreview

interface LessonPreviewRepository {
    suspend fun getLessonPreview(lessonId: String): AppResult<LessonPreview>
}