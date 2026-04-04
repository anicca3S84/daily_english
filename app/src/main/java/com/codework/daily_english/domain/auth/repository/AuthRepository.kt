package com.codework.daily_english.domain.auth.repository

import com.codework.daily_english.core.result.AppResult
import com.codework.daily_english.domain.auth.model.User

interface AuthRepository {
    suspend fun loginWithEmail(email: String, password: String): AppResult<User>
    suspend fun loginWithGoogle(idToken: String): AppResult<User>
    suspend fun loginWithApple(authCode: String): AppResult<User>
}