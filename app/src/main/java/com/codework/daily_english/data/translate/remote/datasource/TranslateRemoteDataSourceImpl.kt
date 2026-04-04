package com.codework.daily_english.data.translate.remote.datasource

import com.codework.daily_english.core.network.safeApiCall
import com.codework.daily_english.core.network.unwrap
import com.codework.daily_english.core.result.AppResult
import com.codework.daily_english.data.translate.remote.TranslateApi
import com.codework.daily_english.data.translate.remote.dto.TranslateDto
import jakarta.inject.Inject

class TranslateRemoteDataSourceImpl @Inject constructor(
    private val api: TranslateApi
) : TranslateRemoteDataSource {

    override suspend fun translate(text: String, source: String, target: String): AppResult<TranslateDto> {
        return safeApiCall { api.translate(text, source, target) }.unwrap()
    }
}