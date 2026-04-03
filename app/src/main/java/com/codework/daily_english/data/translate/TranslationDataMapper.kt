package com.codework.daily_english.data.translate

import com.codework.daily_english.data.translate.local.TranslationEntity
import com.codework.daily_english.data.translate.remote.TranslateDto
import com.codework.daily_english.domain.translate.model.Translation

fun Translation.toEntity() = TranslationEntity(
    id = id,
    sourceText = sourceText,
    targetText = targetText,
    sourceLang = sourceLang,
    targetLang = targetLang,
    timestamp = timestamp
)

fun TranslationEntity.toDomain() = Translation(
    id = id,
    sourceText = sourceText,
    targetText = targetText,
    sourceLang = sourceLang,
    targetLang = targetLang,
    timestamp = timestamp
)

fun TranslateDto.toDomain(
    sourceText: String,
    sourceLang: String,
    targetLang: String
) = Translation(
    sourceText = sourceText,
    targetText = this.translatedText,
    sourceLang = sourceLang,
    targetLang = targetLang,
    timestamp = System.currentTimeMillis()
)