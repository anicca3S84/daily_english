package com.codework.daily_english.domain.translate.usecase

import com.codework.daily_english.core.network.AppError
import com.codework.daily_english.core.network.AppResult
import com.codework.daily_english.core.utils.Constants
import com.codework.daily_english.domain.translate.model.TranslateResult
import com.codework.daily_english.domain.translate.model.Translation
import com.codework.daily_english.domain.translate.repository.TranslateRepository
import javax.inject.Inject

class TranslateTextUseCase @Inject constructor(
    private val repository: TranslateRepository
) {
    suspend operator fun invoke(
        text: String,
        sourceLang: String,
        targetLang: String
    ): AppResult<TranslateResult> {

        val cleanText = text.trim()

        if (cleanText.isBlank()) {
            return AppResult.Error(AppError.Validation(Constants.EMPTY_TEXT))
        }

        if (sourceLang == targetLang) {
            return AppResult.Success(
                TranslateResult(translatedText = cleanText, historyItem = null)
            )
        }

        val result = repository.translateText(cleanText, sourceLang, targetLang)

        return when (result) {
            is AppResult.Success -> {
                val historyItem = Translation(
                    sourceText = cleanText,
                    targetText = result.data,
                    sourceLang = sourceLang,
                    targetLang = targetLang
                )
                AppResult.Success(
                    TranslateResult(
                        translatedText = result.data,
                        historyItem = historyItem
                    )
                )
            }
            is AppResult.Error -> result
        }
    }
}