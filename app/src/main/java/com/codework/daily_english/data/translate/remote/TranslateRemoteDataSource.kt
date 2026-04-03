package com.codework.daily_english.data.translate.remote
import com.codework.daily_english.core.network.safeApiCall
import com.codework.daily_english.core.network.unwrap
import com.codework.daily_english.core.result.AppResult
import jakarta.inject.Inject

interface TranslateRemoteDataSource {
    suspend fun translate(
        text: String,
        source: String,
        target: String
    ): AppResult<TranslateDto>
}

class TranslateRemoteDataSourceImpl @Inject constructor(
    private val api: TranslateApi
) : TranslateRemoteDataSource {

    override suspend fun translate(text: String, source: String, target: String): AppResult<TranslateDto> {
        return safeApiCall { api.translate(text, source, target) }.unwrap()
    }
}