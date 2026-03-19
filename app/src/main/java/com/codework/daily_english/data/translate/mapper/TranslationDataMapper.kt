package com.codework.daily_english.data.translate.mapper

import com.codework.daily_english.data.translate.local.entity.TranslationEntity
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