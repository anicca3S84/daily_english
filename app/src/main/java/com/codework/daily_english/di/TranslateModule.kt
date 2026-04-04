package com.codework.daily_english.di

import com.codework.daily_english.data.translate.local.datasource.TranslateLocalDataSource
import com.codework.daily_english.data.translate.local.datasource.TranslateLocalDataSourceImpl
import com.codework.daily_english.data.translate.repository.TranslateRepositoryImpl
import com.codework.daily_english.data.translate.remote.TranslateApi
import com.codework.daily_english.data.translate.remote.datasource.TranslateRemoteDataSource
import com.codework.daily_english.data.translate.remote.datasource.TranslateRemoteDataSourceImpl
import com.codework.daily_english.domain.translate.repository.TranslateRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TranslateModule {

    companion object{
        @Provides
        @Singleton
        fun provideTranslateApi(retrofit: Retrofit): TranslateApi {
            return retrofit.create(TranslateApi::class.java)
        }
    }

    @Binds
    @Singleton
    abstract fun bindTranslateRepository(
        impl: TranslateRepositoryImpl
    ): TranslateRepository

    @Binds
    @Singleton
    abstract fun bindTranslateLocalDataSource(
        impl: TranslateLocalDataSourceImpl
    ): TranslateLocalDataSource

    @Binds
    @Singleton
    abstract fun bindTranslateRemoteDataSource(
        impl: TranslateRemoteDataSourceImpl
    ): TranslateRemoteDataSource



}