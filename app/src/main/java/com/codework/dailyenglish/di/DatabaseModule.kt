package com.codework.dailyenglish.di

import android.content.Context
import androidx.room.Room
import com.codework.dailyenglish.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "daily_english_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTranslationDao(db: AppDatabase) = db.translationDao()

    @Provides
    @Singleton
    fun provideRecentWordSearchDao(db: AppDatabase) = db.recentWordSearchDao()
}