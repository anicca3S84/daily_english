package com.codework.dailyenglish.data.translate.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.data.translate.remote.dto.TranslateDto

interface TranslateRemoteDataSource {
    suspend fun translate(
        text: String,
        source: String,
        target: String
    ): AppResult<TranslateDto>
}