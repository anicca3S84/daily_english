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
import java.io.File
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: AuthLocalDataSource,
    private val dispatchers: Dispatcher
) : AuthRepository {

    override suspend fun loginWithGoogle(idToken: String): AppResult<User> = withContext(dispatchers.io) {
        val responseResult = remoteDataSource.loginWithGoogle(idToken)

        when (responseResult) {
            is AppResult.Success -> {
                try {
                    val dto = responseResult.data
                    val userDomain = dto.user?.toDomain() ?: return@withContext AppResult.Error(
                        AppError.Validation("User data is missing")
                    )
                    val accessToken = dto.accessToken ?: return@withContext AppResult.Error(
                        AppError.Validation("Access Token is missing")
                    )
                    val refreshToken = dto.refreshToken ?: return@withContext AppResult.Error(
                        AppError.Validation("Refresh Token is missing")
                    )

                    // Lưu local
                    localDataSource.saveTokens(accessToken, refreshToken)
                    localDataSource.saveUser(userDomain)

                    AppResult.Success(userDomain)
                } catch (e: Exception) {
                    // Bắt các lỗi xảy ra trong quá trình lưu local hoặc lỗi xử lý logic cục bộ
                    AppResult.Error(AppError.Unknown(e), message = "Lỗi xử lý dữ liệu nội bộ")
                }
            }
            is AppResult.Error -> {
                responseResult
            }
        }
    }

    override suspend fun loginWithEmail(email: String, password: String): AppResult<User> = withContext(dispatchers.io) {
        TODO("Not yet implemented")
    }

    override suspend fun loginWithApple(authCode: String): AppResult<User> = withContext(dispatchers.io) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserInfo(): User? {
        return localDataSource.getUser()
    }

    override suspend fun getAccessToken(): String? = withContext(dispatchers.io) {
        localDataSource.getAccessToken()
    }

    override suspend fun isUserLoggedIn(): Boolean = withContext(dispatchers.io) {
        val token = localDataSource.getAccessToken()
        !token.isNullOrBlank()
    }

    override suspend fun getMyProfile(): AppResult<User> = withContext(dispatchers.io) {
        val responseResult = remoteDataSource.getMyProfile()
        when (responseResult) {
            is AppResult.Success -> {
                try {
                    val userDomain = responseResult.data.toDomain()
                    localDataSource.saveUser(userDomain)
                    AppResult.Success(userDomain)
                } catch (e: Exception) {
                    AppResult.Error(AppError.Unknown(e), message = "Không thể lưu thông tin tài khoản")
                }
            }
            is AppResult.Error -> {
                responseResult
            }
        }
    }

    override suspend fun updateMyProfile(fullName: String, avatarFile: File?): AppResult<User> = withContext(dispatchers.io) {
        val responseResult = remoteDataSource.updateMyProfile(fullName, avatarFile)
        when (responseResult) {
            is AppResult.Success -> {
                try {
                    val userDomain = responseResult.data.toDomain()
                    localDataSource.saveUser(userDomain)
                    AppResult.Success(userDomain)
                } catch (e: Exception) {
                    AppResult.Error(AppError.Unknown(e), message = "Không thể lưu thông tin tài khoản mới")
                }
            }
            is AppResult.Error -> {
                responseResult
            }
        }
    }

    override suspend fun logout() = withContext(dispatchers.io) {
        try {
            val refreshToken = localDataSource.getRefreshToken()
            if (!refreshToken.isNullOrBlank()) {
                remoteDataSource.logout(refreshToken)
            }
        } catch (e: Exception) {
        } finally {
            localDataSource.clearSession()
        }
    }
}