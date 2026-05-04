package com.codework.dailyenglish.di

import android.content.Context
import com.codework.dailyenglish.presentation.manager.TextToSpeechManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManagerModule {

    @Provides
    @Singleton
    fun provideTTSManager(@ApplicationContext context: Context): TextToSpeechManager {
        return TextToSpeechManager(context)
    }
}