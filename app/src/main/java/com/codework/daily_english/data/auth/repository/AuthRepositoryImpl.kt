package com.codework.daily_english.data.auth.repository

import com.codework.daily_english.core.dispatchers.Dispatcher
import com.codework.daily_english.core.network.AppResult
import com.codework.daily_english.core.network.map
import com.codework.daily_english.data.auth.mapper.toDomain
import com.codework.daily_english.data.auth.local.datasource.AuthLocalDataSource
import com.codework.daily_english.data.auth.remote.datasource.AuthRemoteDataSource
import com.codework.daily_english.domain.auth.model.User
import com.codework.daily_english.domain.auth.repository.AuthRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: AuthLocalDataSource,
    private val dispatchers: Dispatcher
) : AuthRepository {

    override suspend fun loginWithGoogle(idToken: String): AppResult<User> = withContext(dispatchers.io) {
        val responseResult = remoteDataSource.loginWithGoogle(idToken)

        responseResult.map { dto ->
            // 2. Lấy được Token từ Server thì cất ngay xuống Local
            dto.accessToken?.let { token ->
                localDataSource.saveAccessToken(token)
            }

            dto.user?.toDomain() ?: throw IllegalStateException("User data is missing")
        }
    }

    override suspend fun loginWithEmail(email: String, password: String): AppResult<User> = withContext(dispatchers.io) {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithApple(authCode: String): AppResult<User> = withContext(dispatchers.io) {
        TODO("Not yet implemented")
    }
}