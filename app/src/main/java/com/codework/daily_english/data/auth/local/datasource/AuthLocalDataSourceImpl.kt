package com.codework.daily_english.data.auth.local.datasource

import com.codework.daily_english.core.dispatchers.Dispatcher
import com.codework.daily_english.data.auth.local.AuthPreferences
import com.codework.daily_english.domain.auth.model.User
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthLocalDataSourceImpl @Inject constructor(
    private val authPreferences: AuthPreferences,
    private val dispatchers: Dispatcher
) : AuthLocalDataSource {

    override suspend fun saveTokens(accessToken: String, refreshToken: String) = withContext(dispatchers.io) {
        authPreferences.saveTokens(accessToken, refreshToken)
    }

    override suspend fun saveAccessToken(token: String) = withContext(dispatchers.io) {
        authPreferences.saveAccessToken(token)
    }

    override suspend fun saveRefreshToken(token: String) = withContext(dispatchers.io) {
        authPreferences.saveRefreshToken(token)
    }

    override suspend fun saveUser(user: User) = withContext(dispatchers.io) {
        authPreferences.saveUser(
            userId = user.id,
            email = user.email,
            fullName = user.fullName,
            avatarUrl = user.avatarUrl
        )
    }

    override suspend fun getAccessToken(): String? = withContext(dispatchers.io) {
        authPreferences.getAccessToken()
    }

    override suspend fun getRefreshToken(): String? = withContext(dispatchers.io) {
        authPreferences.getRefreshToken()
    }

    override suspend fun clearSession() = withContext(dispatchers.io) {
        authPreferences.clearAll()
    }
}