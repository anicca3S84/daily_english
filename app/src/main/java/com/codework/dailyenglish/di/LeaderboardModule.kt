package com.codework.dailyenglish.di

import com.codework.dailyenglish.data.leaderboard.remote.LeaderBoardApi
import com.codework.dailyenglish.data.leaderboard.remote.datasource.LeaderBoardRemoteDataSource
import com.codework.dailyenglish.data.leaderboard.remote.datasource.LeaderBoardRemoteDataSourceImpl
import com.codework.dailyenglish.data.leaderboard.remote.repository.LeaderBoardRepositoryImpl
import com.codework.dailyenglish.domain.leaderboard.repository.LeaderBoardRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LeaderboardModule {

    companion object {

        @Provides
        @Singleton
        fun provideLeaderBoardApi(
            retrofit: Retrofit
        ): LeaderBoardApi {
            return retrofit.create(LeaderBoardApi::class.java)
        }
    }

    @Binds
    @Singleton
    abstract fun bindLeaderBoardRemoteDataSource(
        impl: LeaderBoardRemoteDataSourceImpl
    ): LeaderBoardRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindLeaderBoardRepository(
        impl: LeaderBoardRepositoryImpl
    ): LeaderBoardRepository
}