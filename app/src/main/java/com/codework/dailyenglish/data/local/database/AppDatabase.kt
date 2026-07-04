package com.codework.dailyenglish.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codework.dailyenglish.data.dictionary.local.RecentWordSearchDao
import com.codework.dailyenglish.data.dictionary.local.RecentWordSearchEntity
import com.codework.dailyenglish.data.translate.local.TranslationDao
import com.codework.dailyenglish.data.translate.local.TranslationEntity

@Database(entities = [TranslationEntity::class, RecentWordSearchEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun translationDao(): TranslationDao
    abstract fun recentWordSearchDao(): RecentWordSearchDao
}