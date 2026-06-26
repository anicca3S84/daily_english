package com.codework.dailyenglish.di



import com.codework.dailyenglish.data.vocabulary.remote.VocabularyApi
import com.codework.dailyenglish.data.vocabulary.remote.datasource.VocabularyRemoteDataSource
import com.codework.dailyenglish.data.vocabulary.remote.datasource.VocabularyRemoteDataSourceImpl
import com.codework.dailyenglish.data.vocabulary.remote.repository.VocabularyRepositoryImpl
import com.codework.dailyenglish.domain.vocabulary.repository.VocabularyRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class VocabularyModule {

    companion object {
        @Provides
        @Singleton
        fun provideVocabularyApi(retrofit: Retrofit): VocabularyApi {
            return retrofit.create(VocabularyApi::class.java)
        }
    }

    @Binds
    @Singleton
    abstract fun bindVocabularyRemoteDataSource(
        impl: VocabularyRemoteDataSourceImpl
    ): VocabularyRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindVocabularyRepository(
        impl: VocabularyRepositoryImpl
    ): VocabularyRepository
}