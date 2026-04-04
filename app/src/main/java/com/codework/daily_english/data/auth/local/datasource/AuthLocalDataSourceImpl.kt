package com.codework.daily_english.data.auth.local.datasource

import com.codework.daily_english.core.dispatchers.Dispatcher
import com.codework.daily_english.data.auth.local.AuthPreferences
import jakarta.inject.Inject
import kotlinx.coroutines.withContext

class AuthLocalDataSourceImpl @Inject constructor(
    private val authPreferences: AuthPreferences,
    private val dispatchers: Dispatcher
) : AuthLocalDataSource {

    override suspend fun saveAccessToken(token: String) = withContext(dispatchers.io) {
        authPreferences.saveAccessToken(token)
    }

    override suspend fun getAccessToken(): String? = withContext(dispatchers.io) {
        authPreferences.getAccessToken()
    }

    override suspend fun clearSession() = withContext(dispatchers.io) {
        authPreferences.clearSession()
    }
}