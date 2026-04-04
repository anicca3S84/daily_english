package com.codework.daily_english.data.translate.remote.datasource

import com.codework.daily_english.core.network.AppResult
import com.codework.daily_english.data.translate.remote.dto.TranslateDto

interface TranslateRemoteDataSource {
    suspend fun translate(
        text: String,
        source: String,
        target: String
    ): AppResult<TranslateDto>
}