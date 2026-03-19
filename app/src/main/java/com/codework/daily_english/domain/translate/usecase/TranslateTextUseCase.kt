package com.codework.daily_english.domain.translate.usecase

import com.codework.daily_english.core.result.AppError
import com.codework.daily_english.core.result.AppResult
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
    ): AppResult<TranslateResult> { // ✅ Bỏ <...> vì TranslateResult đã cụ thể hóa kiểu dữ liệu rồi

        val cleanText = text.trim()

        if (cleanText.isBlank()) {
            return AppResult.Error(AppError.Validation("EMPTY_TEXT"))
        }

        // Nếu ngôn ngữ giống nhau, vẫn trả về TranslateResult nhưng historyItem = null
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
            // Ép kiểu result về AppResult.Error để tương thích với kiểu trả về của hàm
            is AppResult.Error -> result
        }
    }
}