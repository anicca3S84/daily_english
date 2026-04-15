package com.codework.dailyenglish.data.auth.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.data.auth.remote.dto.AuthDto

interface AuthRemoteDataSource {
    suspend fun loginWithGoogle(idToken: String): AppResult<AuthDto>
}