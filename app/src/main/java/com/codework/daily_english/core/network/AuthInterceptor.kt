package com.codework.daily_english.core.network

import com.codework.daily_english.data.auth.local.AuthPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val prefs: AuthPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestUrl = originalRequest.url.encodedPath

        // 1. CHẶN LẠI NGAY ĐÂY: Nếu đường dẫn có chứa "/auth"
        // (nghĩa là đang gọi login Google, Apple, Email hoặc Refresh token)
        // thì KHÔNG ĐƯỢC gắn Header Authorization, cho đi qua luôn!
        if (requestUrl.contains("/auth")) {
            return chain.proceed(originalRequest)
        }

        // 2. Nếu là các API bình thường (lấy thông tin user, bài học...)
        // thì mới lấy Token ra và gắn vào Header
        val token = runBlocking { prefs.getAccessToken() }
        val request = originalRequest.newBuilder().apply {
            token?.let {
                addHeader("Authorization", "Bearer $it")
            }
        }.build()

        return chain.proceed(request)
    }
}