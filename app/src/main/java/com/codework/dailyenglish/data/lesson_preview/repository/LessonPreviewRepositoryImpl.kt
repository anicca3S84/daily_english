package com.codework.dailyenglish.data.lesson_preview.repository

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.map
import com.codework.dailyenglish.data.lesson_preview.mapper.toDomain
import com.codework.dailyenglish.data.lesson_preview.remote.datasource.LessonPreviewRemoteDataSource
import com.codework.dailyenglish.domain.lesson_preview.model.LessonPreview
import com.codework.dailyenglish.domain.lesson_preview.repository.LessonPreviewRepository
import javax.inject.Inject

class LessonPreviewRepositoryImpl @Inject constructor(
    private val remoteDataSource: LessonPreviewRemoteDataSource
) : LessonPreviewRepository {
    override suspend fun getLessonPreview(lessonId: String): AppResult<LessonPreview> {
        return remoteDataSource.getLessonPreview(lessonId).map { it.toDomain() }
    }
}