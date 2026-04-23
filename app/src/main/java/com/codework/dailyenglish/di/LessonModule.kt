package com.codework.dailyenglish.di

import com.codework.dailyenglish.data.lesson.LessonApi
import com.codework.dailyenglish.data.lesson.remote.datasource.LessonRemoteDataSource
import com.codework.dailyenglish.data.lesson.remote.datasource.LessonRemoteDataSourceImpl
import com.codework.dailyenglish.data.lesson.repository.LessonRepositoryImpl
import com.codework.dailyenglish.domain.lesson.repository.LessonRepository
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LessonModule {

    companion object {
        @Provides
        @Singleton
        fun provideLessonApi(retrofit: Retrofit): LessonApi {
            return retrofit.create(LessonApi::class.java)
        }
    }

    @Binds
    @Singleton
    abstract fun bindLessonRepository(
        impl: LessonRepositoryImpl
    ): LessonRepository

    @Binds
    @Singleton
    abstract fun bindLessonRemoteDataSource(
        impl: LessonRemoteDataSourceImpl
    ): LessonRemoteDataSource

}