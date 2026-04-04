package com.codework.daily_english.data.auth.remote.datasource

import com.codework.daily_english.core.result.AppResult
import com.codework.daily_english.data.auth.remote.dto.AuthDto

interface AuthRemoteDataSource {
    suspend fun loginWithGoogle(idToken: String): AppResult<AuthDto>
}