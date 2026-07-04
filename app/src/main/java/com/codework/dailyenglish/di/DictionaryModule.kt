package com.codework.dailyenglish.di

import com.codework.dailyenglish.data.dictionary.local.datasource.DictionaryLocalDataSource
import com.codework.dailyenglish.data.dictionary.local.datasource.DictionaryLocalDataSourceImpl
import com.codework.dailyenglish.data.dictionary.remote.DictionaryApi
import com.codework.dailyenglish.data.dictionary.remote.datasource.DictionaryRemoteDataSource
import com.codework.dailyenglish.data.dictionary.remote.datasource.DictionaryRemoteDataSourceImpl
import com.codework.dailyenglish.data.dictionary.repository.DictionaryRepositoryImpl
import com.codework.dailyenglish.domain.dictionary.repository.DictionaryRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DictionaryModule {

    companion object {
        @Provides
        @Singleton
        fun provideDictionaryApi(retrofit: Retrofit): DictionaryApi {
            return retrofit.create(DictionaryApi::class.java)
        }
    }

    @Binds
    @Singleton
    abstract fun bindDictionaryRepository(
        impl: DictionaryRepositoryImpl
    ): DictionaryRepository

    @Binds
    @Singleton
    abstract fun bindDictionaryRemoteDataSource(
        impl: DictionaryRemoteDataSourceImpl
    ): DictionaryRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindDictionaryLocalDataSource(
        impl: DictionaryLocalDataSourceImpl
    ): DictionaryLocalDataSource
}