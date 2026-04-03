package com.codework.daily_english.di

import com.codework.daily_english.data.course.remote.CourseRemoteDataSource
import com.codework.daily_english.data.course.remote.CourseRemoteDataSourceImpl
import com.codework.daily_english.data.course.CourseRepositoryImpl
import com.codework.daily_english.domain.course.repository.CourseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CourseModule {

    @Binds
    @Singleton
    abstract fun bindCourseRepository(
        impl: CourseRepositoryImpl
    ): CourseRepository

    @Binds
    @Singleton
    abstract fun bindCourseRemoteDataSource(
        impl: CourseRemoteDataSourceImpl
    ): CourseRemoteDataSource


}