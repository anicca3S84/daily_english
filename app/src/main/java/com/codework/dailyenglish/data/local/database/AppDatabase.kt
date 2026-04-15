package com.codework.dailyenglish.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codework.dailyenglish.data.translate.local.TranslationDao
import com.codework.dailyenglish.data.translate.local.TranslationEntity

@Database(entities = [TranslationEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun translationDao(): TranslationDao
}