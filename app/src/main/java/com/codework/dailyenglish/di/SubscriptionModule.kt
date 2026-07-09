package com.codework.dailyenglish.di

import com.codework.dailyenglish.data.subscription.remote.SubscriptionApi
import com.codework.dailyenglish.data.subscription.remote.datasource.SubscriptionRemoteDataSource
import com.codework.dailyenglish.data.subscription.remote.datasource.SubscriptionRemoteDataSourceImpl
import com.codework.dailyenglish.data.subscription.repository.SubscriptionRepositoryImpl
import com.codework.dailyenglish.domain.subscription.repository.SubscriptionRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SubscriptionModule {

    companion object{
        @Provides
        @Singleton
        fun provideSubscriptionApi(retrofit: Retrofit): SubscriptionApi {
            return retrofit.create(SubscriptionApi::class.java)
        }
    }

    @Binds
    @Singleton
    abstract fun bindSubscriptionRepository(
        impl: SubscriptionRepositoryImpl
    ): SubscriptionRepository

    @Binds
    @Singleton
    abstract fun bindSubscriptionRemoteDataSource(
        impl: SubscriptionRemoteDataSourceImpl
    ): SubscriptionRemoteDataSource

}