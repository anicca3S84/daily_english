package com.codework.dailyenglish.di

import com.codework.dailyenglish.data.ai.remote.AiApiService
import com.codework.dailyenglish.data.ai.remote.datasource.AiRemoteDataSource
import com.codework.dailyenglish.data.ai.remote.datasource.AiRemoteDataSourceImpl
import com.codework.dailyenglish.data.ai.repository.AiRepositoryImpl
import com.codework.dailyenglish.domain.ai.repository.AiRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AiNetworkModule {
    companion object {
        @Provides
        @Singleton
        fun provideAiApiService(
            retrofit: Retrofit
        ): AiApiService {
            return retrofit.create(AiApiService::class.java)
        }
    }
    @Binds
    @Singleton
    abstract fun bindAiRemoteDataSource(
        impl: AiRemoteDataSourceImpl
    ): AiRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAiRepository(
        impl: AiRepositoryImpl
    ): AiRepository
}