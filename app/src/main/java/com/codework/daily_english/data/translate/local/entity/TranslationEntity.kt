package com.codework.daily_english.data.translate.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translations")
data class TranslationEntity(
    @PrimaryKey
    val id: String,
    val sourceText: String,
    val targetText: String,
    val sourceLang: String,
    val targetLang: String,
    val timestamp: Long
)