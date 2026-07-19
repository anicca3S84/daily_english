package com.codework.dailyenglish.core.network

import com.codework.dailyenglish.core.session.SessionEvent
import com.codework.dailyenglish.core.session.SessionEventBus
import com.codework.dailyenglish.data.auth.local.datasource.AuthLocalDataSource
import com.codework.dailyenglish.data.auth.remote.AuthApi
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

class TokenAuthenticator @Inject constructor(
    private val authApiProvider: Provider<AuthApi>,
    private val localDataSource: AuthLocalDataSource,
    private val sessionEventBus: SessionEventBus
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // 1. CHẶN VÒNG LẶP: Nếu request bị lỗi 401 chính là API refresh token,
        // trả về null để OkHttp dừng lại và ném lỗi ra ngoài, tránh lặp vô tận.
        if (response.request.url.encodedPath.contains("auth/refresh")) {
            return null
        }

        if (response.code != 401) return null

        val refreshToken = runBlocking { localDataSource.getRefreshToken() }
        if (refreshToken.isNullOrBlank()) {
            handleForceLogout()
            return null
        }

        synchronized(this) {
            val currentToken = runBlocking { localDataSource.getAccessToken() }
            val requestHeaderToken = response.request.header("Authorization")?.replace("Bearer ", "")

            if (currentToken != requestHeaderToken) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $currentToken")
                    .build()
            }

            return try {
                val authApi = authApiProvider.get()
                val refreshResponse = authApi.refreshToken("Bearer $refreshToken").execute()

                if (refreshResponse.isSuccessful && refreshResponse.body() != null) {
                    val newAuthDto = refreshResponse.body()?.data
                    val newAccessToken = newAuthDto?.accessToken.orEmpty()
                    val newRefreshToken = newAuthDto?.refreshToken.orEmpty()

                    runBlocking {
                        localDataSource.saveTokens(newAccessToken, newRefreshToken)
                    }

                    response.request.newBuilder()
                        .header("Authorization", "Bearer $newAccessToken")
                        .build()
                } else {
                    // Nếu gọi refresh thất bại (ví dụ trả về 401 do hết hạn)
                    handleForceLogout()
                    null
                }
            } catch (e: Exception) {
                handleForceLogout()
                null
            }
        }
    }

    private fun handleForceLogout() {
        runBlocking {
            localDataSource.clearSession()
            sessionEventBus.emit(SessionEvent.ForceLogout)
        }
    }
}