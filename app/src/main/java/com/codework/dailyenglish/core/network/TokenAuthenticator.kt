package com.codework.dailyenglish.core.network

import com.codework.dailyenglish.data.auth.local.AuthPreferences
import com.codework.dailyenglish.data.auth.remote.AuthApi
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Provider

class TokenAuthenticator @Inject constructor(
    private val prefs: AuthPreferences,
    private val authApi: Provider<AuthApi>
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = prefs.getRefreshToken() ?: return null

        val res = authApi.get().refreshToken("Bearer $refreshToken").execute()
        val authDto = res.body()

        return if (res.isSuccessful && authDto != null &&
            authDto.accessToken != null && authDto.refreshToken != null) {
            prefs.saveAccessToken(authDto.accessToken)
            prefs.saveRefreshToken(authDto.refreshToken)
            response.request.newBuilder()
                .header("Authorization", "Bearer ${authDto.accessToken}")
                .build()
        } else {
            prefs.clearAll()
            null
        }
    }
}