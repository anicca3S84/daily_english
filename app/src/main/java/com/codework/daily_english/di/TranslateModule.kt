package com.codework.daily_english.di

import com.codework.daily_english.data.translate.local.TranslateLocalDataSource
import com.codework.daily_english.data.translate.local.TranslateLocalDataSourceImpl
import com.codework.daily_english.data.translate.remote.TranslateRemoteDataSource
import com.codework.daily_english.data.translate.remote.TranslateRemoteDataSourceImpl
import com.codework.daily_english.data.translate.repository.TranslateRepositoryImpl
import com.codework.daily_english.domain.translate.repository.TranslateRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TranslateModule {

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