package com.codework.dailyenglish.presentation.screen.translate.mapper

import com.codework.dailyenglish.domain.translate.model.Translation
import com.codework.dailyenglish.presentation.screen.translate.model.TranslationUi
import com.codework.dailyenglish.presentation.utils.DateUtils

fun Translation.toUiModel(): TranslationUi {
    return TranslationUi(
        id = id,
        sourceText = sourceText,
        targetText = targetText,
        formattedTime = DateUtils.formatTime(timestamp)
    )
}

fun List<Translation>.toUiModels(): List<TranslationUi> {
    return this.map { it.toUiModel() }
}