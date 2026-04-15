package com.codework.dailyenglish.data.auth.repository

import com.codework.dailyenglish.core.dispatchers.Dispatcher
import com.codework.dailyenglish.core.network.AppError
import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.map
import com.codework.dailyenglish.data.auth.mapper.toDomain
import com.codework.dailyenglish.data.auth.local.datasource.AuthLocalDataSource
import com.codework.dailyenglish.data.auth.remote.datasource.AuthRemoteDataSource
import com.codework.dailyenglish.domain.auth.model.User
import com.codework.dailyenglish.domain.auth.repository.AuthRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: AuthLocalDataSource,
    private val dispatchers: Dispatcher
) : AuthRepository {

    override suspend fun loginWithGoogle(idToken: String): AppResult<User> = withContext(dispatchers.io) {
        try {
            val responseResult = remoteDataSource.loginWithGoogle(idToken)

            responseResult.map { dto ->
                val userDomain = dto.user?.toDomain() ?: throw Exception("User data is missing")
                val accessToken = dto.accessToken ?: throw Exception("Access Token is missing")
                val refreshToken = dto.refreshToken ?: throw Exception("Refresh Token is missing")

                localDataSource.saveTokens(accessToken, refreshToken)

                localDataSource.saveUser(userDomain)

                userDomain
            }
        } catch (e: Exception) {
            // Hứng lỗi và trả về chuẩn AppError của bạn
            AppResult.Error(AppError.Unknown(e))
        }
    }

    override suspend fun loginWithEmail(email: String, password: String): AppResult<User> = withContext(dispatchers.io) {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithApple(authCode: String): AppResult<User> = withContext(dispatchers.io) {
        TODO("Not yet implemented")
    }
}