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
        // Gọi API qua RemoteDataSource (Đã được bọc safeApiCall bên trong)
        val responseResult = remoteDataSource.loginWithGoogle(idToken)

        // Phân rã kết quả trả về bằng when để kiểm soát hoàn toàn luồng dữ liệu
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
                // 🎯 TRẢ VỀ NGUYÊN VẸN: Giữ nguyên AppError.Http và cái "message" từ server (Ví dụ: Bạn đã hết lượt...)
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

    override suspend fun logout() = withContext(dispatchers.io) {
        localDataSource.clearSession()
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
}