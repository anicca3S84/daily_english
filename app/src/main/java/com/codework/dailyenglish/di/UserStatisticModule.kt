package com.codework.dailyenglish.di

import com.codework.dailyenglish.data.user_statistic.remote.StatisticApi
import com.codework.dailyenglish.data.user_statistic.remote.datasource.UserStatisticRemoteDataSource
import com.codework.dailyenglish.data.user_statistic.remote.datasource.UserStatisticRemoteDataSourceImpl
import com.codework.dailyenglish.data.user_statistic.repository.UserStatisticRepositoryImpl
import com.codework.dailyenglish.domain.user_statistic.repository.UserStatisticRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserStatisticModule {
    companion object {
        @Provides
        @Singleton
        fun provideUserStatisticApi(retrofit: Retrofit): StatisticApi {
            return retrofit.create(StatisticApi::class.java)
        }
    }


    @Binds
    @Singleton
    abstract fun bindUserStatisticRepository(
        impl: UserStatisticRepositoryImpl
    ): UserStatisticRepository

    @Binds
    @Singleton
    abstract fun bindUserStatisticRemoteDataSource(
        impl: UserStatisticRemoteDataSourceImpl
    ): UserStatisticRemoteDataSource
}