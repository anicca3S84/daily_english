package com.codework.daily_english.di

import android.content.Context
import androidx.room.Room
import com.codework.daily_english.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
// ✅ SỬA DÒNG NÀY: Từ jakarta.inject sang javax.inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton // Giờ đây Hilt đã hiểu đây là Singleton của nó
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "daily_english_db"
        ).build()
    }

    @Provides
    @Singleton // Thêm Singleton ở đây nếu bạn muốn dùng chung 1 instance Dao
    fun provideTranslationDao(db: AppDatabase) = db.translationDao()
}