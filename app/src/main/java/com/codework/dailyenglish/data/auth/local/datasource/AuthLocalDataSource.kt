package com.codework.dailyenglish.data.auth.local.datasource

import com.codework.dailyenglish.domain.auth.model.User

interface AuthLocalDataSource {
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun saveUser(user: User)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun clearSession()
    suspend fun saveRefreshToken(token: String)
    suspend fun saveAccessToken(token: String)
}