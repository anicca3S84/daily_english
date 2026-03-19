package com.codework.daily_english.data.translate.datasource.remote

import com.codework.daily_english.data.translate.remote.TranslateApi
import com.codework.daily_english.data.translate.remote.dto.TranslateResponseDto
import jakarta.inject.Inject

interface TranslateRemoteDataSource {
    suspend fun translate(text: String, source: String, target: String): TranslateResponseDto
}

class TranslateRemoteDataSourceImpl @Inject constructor(
    private val api: TranslateApi
) : TranslateRemoteDataSource {
    override suspend fun translate(text: String, source: String, target: String) =
        api.translate(text, source, target)
}