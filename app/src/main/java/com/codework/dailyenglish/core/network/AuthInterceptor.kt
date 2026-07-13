package com.codework.dailyenglish.core.network

import com.codework.dailyenglish.data.auth.local.AuthPreferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.util.Locale
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val prefs: AuthPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestUrl = originalRequest.url.encodedPath

//        var currentLanguage = Locale.getDefault().language.uppercase()
        var currentLanguage = "EN" //TODO(Multiple Languages)
        if (currentLanguage != "VI" && currentLanguage != "EN") {
            currentLanguage = "EN"
        }

        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Accept-Language", currentLanguage)

        if (requestUrl.contains("/auth")) {
            return chain.proceed(requestBuilder.build())
        }

        val token = runBlocking { prefs.getAccessToken() }
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}