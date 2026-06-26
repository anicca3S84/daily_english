package com.codework.dailyenglish.di

import android.content.Context
import com.codework.dailyenglish.data.user_statistic.local.AppPreferences
import com.codework.dailyenglish.data.user_statistic.local.AppPreferencesImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideAppPreferences(
        @ApplicationContext context: Context
    ): AppPreferences {
        return AppPreferencesImpl(context)
    }
}