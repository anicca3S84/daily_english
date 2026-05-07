package com.codework.dailyenglish.data.lesson.repository

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.map
import com.codework.dailyenglish.data.lesson.mapper.toDomain
import com.codework.dailyenglish.data.lesson.remote.datasource.LessonRemoteDataSource
import com.codework.dailyenglish.domain.lesson.model.LessonPlay
import com.codework.dailyenglish.domain.lesson.repository.LessonRepository
import com.codework.dailyenglish.data.lesson.mapper.toResultDomain
import com.codework.dailyenglish.domain.lesson.model.LessonResult
import javax.inject.Inject

class LessonRepositoryImpl @Inject constructor(
    private val remoteDataSource: LessonRemoteDataSource
) : LessonRepository {

    override suspend fun getLessonPlayData(lessonId: String): AppResult<LessonPlay> {
        return remoteDataSource.getLessonPlayData(lessonId).map { dto ->
            dto.toDomain()
        }
    }

    override suspend fun updateLessonProgress(
        lessonId: String,
        cursor: Int,
        questionId: String?,
        optionId: String?,
        userAnswer: String?,
        selectedIndices: List<Int>?,
        isCorrect: Boolean?
    ): AppResult<Unit> {
        return remoteDataSource.updateLessonProgress(
            lessonId, cursor, questionId, optionId,userAnswer, selectedIndices,isCorrect
        )
    }

    override suspend fun completeLesson(lessonPublicId: String): AppResult<LessonResult> {
        return remoteDataSource.completeLesson(lessonPublicId).map { dto ->
            dto.toResultDomain()
        }
    }

    override suspend fun reviewLesson(lessonId: String): AppResult<LessonPlay> {
        return remoteDataSource.reviewLesson(lessonId).map { dto ->
            dto.toDomain()
        }
    }
}