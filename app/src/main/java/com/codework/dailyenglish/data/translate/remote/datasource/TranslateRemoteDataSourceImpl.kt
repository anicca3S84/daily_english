package com.codework.dailyenglish.data.translate.remote.datasource

import com.codework.dailyenglish.core.network.safeApiCall
import com.codework.dailyenglish.core.network.unwrap
import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.data.translate.remote.TranslateApi
import com.codework.dailyenglish.data.translate.remote.dto.TranslateDto
import com.codework.dailyenglish.data.translate.remote.dto.TranslateRequestDto
import jakarta.inject.Inject

class TranslateRemoteDataSourceImpl @Inject constructor(
    private val api: TranslateApi
) : TranslateRemoteDataSource {

    override suspend fun translate(translateRequest: TranslateRequestDto): AppResult<TranslateDto> {
        return safeApiCall { api.translate(translateRequest) }.unwrap()
    }
}