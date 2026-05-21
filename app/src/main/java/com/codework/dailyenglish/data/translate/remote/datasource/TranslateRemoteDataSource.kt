package com.codework.dailyenglish.data.translate.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.data.translate.remote.dto.TranslateDto
import com.codework.dailyenglish.data.translate.remote.dto.TranslateRequestDto

interface TranslateRemoteDataSource {
    suspend fun translate(
        translateRequest: TranslateRequestDto
    ): AppResult<TranslateDto>
}