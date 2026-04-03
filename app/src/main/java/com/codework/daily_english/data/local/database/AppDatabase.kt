package com.codework.daily_english.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codework.daily_english.data.translate.local.TranslationDao
import com.codework.daily_english.data.translate.local.TranslationEntity

@Database(entities = [TranslationEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun translationDao(): TranslationDao
}