package com.codework.daily_english.di

import com.codework.daily_english.data.auth.local.datasource.AuthLocalDataSource
import com.codework.daily_english.data.auth.local.datasource.AuthLocalDataSourceImpl
import com.codework.daily_english.data.auth.remote.AuthApi
import com.codework.daily_english.data.auth.remote.datasource.AuthRemoteDataSource
import com.codework.daily_english.data.auth.remote.datasource.AuthRemoteDataSourceImpl
import com.codework.daily_english.data.auth.repository.AuthRepositoryImpl
import com.codework.daily_english.domain.auth.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthBindModule {

    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(
        impl: AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindAuthLocalDataSource(
        impl: AuthLocalDataSourceImpl
    ): AuthLocalDataSource

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    companion object {
        @Provides
        @Singleton
        fun provideAuthApi(retrofit: Retrofit): AuthApi {
            return retrofit.create(AuthApi::class.java)
        }
    }
}