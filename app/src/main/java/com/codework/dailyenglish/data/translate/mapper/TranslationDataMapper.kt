package com.codework.dailyenglish.data.translate.mapper

import com.codework.dailyenglish.data.translate.local.TranslationEntity
import com.codework.dailyenglish.data.translate.remote.dto.TranslateDto
import com.codework.dailyenglish.domain.translate.model.Translation

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