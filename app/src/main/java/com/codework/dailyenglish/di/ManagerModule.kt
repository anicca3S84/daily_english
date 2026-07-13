package com.codework.dailyenglish.di

import android.content.Context
import com.codework.dailyenglish.core.utils.TextToSpeechParser
import com.codework.dailyenglish.core.utils.VoiceToTextParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManagerModule {

    @Provides
    @Singleton
    fun provideTextToSpeechParser(
        @ApplicationContext context: Context
    ): TextToSpeechParser {
        return TextToSpeechParser(context)
    }
}

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelManagerModule {

    @Provides
    @ViewModelScoped
    fun provideVoiceToTextParser(): VoiceToTextParser {
        return VoiceToTextParser()
    }
}