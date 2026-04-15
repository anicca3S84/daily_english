package com.codework.dailyenglish.di

import com.codework.dailyenglish.data.course.remote.CourseApi
import com.codework.dailyenglish.data.course.remote.datasource.CourseRemoteDataSource
import com.codework.dailyenglish.data.course.remote.datasource.CourseRemoteDataSourceImpl
import com.codework.dailyenglish.data.course.repository.CourseRepositoryImpl
import com.codework.dailyenglish.domain.course.repository.CourseRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CourseModule {

    companion object {
        @Provides
        @Singleton
        fun provideCourseApi(retrofit: Retrofit): CourseApi {
            return retrofit.create(CourseApi::class.java)
        }
    }

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