package com.codework.dailyenglish.di

import com.codework.dailyenglish.data.lesson_preview.remote.LessonPreviewApi
import com.codework.dailyenglish.data.lesson_preview.remote.datasource.LessonPreviewRemoteDataSource
import com.codework.dailyenglish.data.lesson_preview.remote.datasource.LessonPreviewRemoteDataSourceImpl
import com.codework.dailyenglish.data.lesson_preview.repository.LessonPreviewRepositoryImpl
import com.codework.dailyenglish.domain.lesson_preview.repository.LessonPreviewRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LessonPreviewModule {

    companion object {
        @Provides
        @Singleton
        fun provideLessonPreviewApi(retrofit: Retrofit): LessonPreviewApi {
            return retrofit.create(LessonPreviewApi::class.java)
        }
    }

    @Binds
    @Singleton
    abstract fun bindLessonPreviewRepository(
        impl: LessonPreviewRepositoryImpl
    ): LessonPreviewRepository

    @Binds
    @Singleton
    abstract fun bindLessonPreviewRemoteDataSource(
        impl: LessonPreviewRemoteDataSourceImpl
    ): LessonPreviewRemoteDataSource
}