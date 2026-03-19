package com.codework.daily_english.di

import com.codework.daily_english.data.translate.datasource.local.TranslateLocalDataSource
import com.codework.daily_english.data.translate.datasource.local.TranslateLocalDataSourceImpl
import com.codework.daily_english.data.translate.datasource.remote.TranslateRemoteDataSource
import com.codework.daily_english.data.translate.datasource.remote.TranslateRemoteDataSourceImpl
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

    // 1. Kết nối Interface Repository với Implementation của nó
    @Binds
    @Singleton
    abstract fun bindTranslateRepository(
        impl: TranslateRepositoryImpl
    ): TranslateRepository

    // 2. Kết nối Local DataSource
    @Binds
    @Singleton
    abstract fun bindTranslateLocalDataSource(
        impl: TranslateLocalDataSourceImpl
    ): TranslateLocalDataSource

    // 3. Kết nối Remote DataSource
    @Binds
    @Singleton
    abstract fun bindTranslateRemoteDataSource(
        impl: TranslateRemoteDataSourceImpl
    ): TranslateRemoteDataSource
}