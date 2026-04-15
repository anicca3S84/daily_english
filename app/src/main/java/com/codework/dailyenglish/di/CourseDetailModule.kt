package com.codework.dailyenglish.di

import com.codework.dailyenglish.data.course_detail.remote.CourseDetailApi
import com.codework.dailyenglish.data.course_detail.repository.CourseDetailRepositoryImpl
import com.codework.dailyenglish.data.course_detail.remote.datasource.CourseDetailRemoteDataSource
import com.codework.dailyenglish.data.course_detail.remote.datasource.CourseDetailRemoteDataSourceImpl
import com.codework.dailyenglish.domain.course_detail.repository.CourseDetailRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CourseDetailModule {

    companion object {
        @Provides
        @Singleton
        fun provideCourseDetailApi(retrofit: Retrofit): CourseDetailApi {
            return retrofit.create(CourseDetailApi::class.java)
        }
    }


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