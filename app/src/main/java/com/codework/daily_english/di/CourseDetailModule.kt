package com.codework.daily_english.di

import com.codework.daily_english.data.course_detail.repository.CourseDetailRepositoryImpl
import com.codework.daily_english.data.course_detail.remote.CourseDetailRemoteDataSource
import com.codework.daily_english.data.course_detail.remote.CourseDetailRemoteDataSourceImpl
import com.codework.daily_english.domain.course_detail.repository.CourseDetailRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CourseDetailModule {
    @Binds
    @Singleton
    abstract fun bindCourseDetailRepository(
        impl: CourseDetailRepositoryImpl
    ): CourseDetailRepository

    @Binds
    @Singleton
    abstract fun bindCourseDetailRemoteDataSource(
        impl: CourseDetailRemoteDataSourceImpl
    ): CourseDetailRemoteDataSource
}