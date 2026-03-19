package com.codework.daily_english.presentation.screen.translate.mapper

import com.codework.daily_english.domain.translate.model.Translation
import com.codework.daily_english.presentation.screen.translate.model.TranslationUiModel
import com.codework.daily_english.presentation.utils.DateUtils

fun Translation.toUiModel(): TranslationUiModel {
    return TranslationUiModel(
        id = id,
        sourceText = sourceText,
        targetText = targetText,
        formattedTime = DateUtils.formatTime(timestamp)
    )
}

fun List<Translation>.toUiModels(): List<TranslationUiModel> {
    return this.map { it.toUiModel() }
}