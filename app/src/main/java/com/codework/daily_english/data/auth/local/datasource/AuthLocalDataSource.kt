package com.codework.daily_english.data.auth.local.datasource

interface AuthLocalDataSource {
    suspend fun saveAccessToken(token: String)
    suspend fun getAccessToken(): String?
    suspend fun clearSession()
}