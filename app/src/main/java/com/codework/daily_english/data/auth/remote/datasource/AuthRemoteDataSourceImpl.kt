package com.codework.daily_english.data.auth.remote.datasource

import com.codework.daily_english.core.result.AppResult
import com.codework.daily_english.data.auth.remote.AuthApi
import com.codework.daily_english.data.auth.remote.dto.AuthDto
import com.codework.daily_english.data.auth.remote.dto.UserDto
import kotlinx.coroutines.delay
import java.util.UUID
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val api: AuthApi
) : AuthRemoteDataSource {

    override suspend fun loginWithGoogle(idToken: String): AppResult<AuthDto> {
        // return safeApiCall { api.loginWithGoogle(GoogleLoginRequest(idToken)) }.unwrap()

        // FAKE DATA ĐỂ TEST UI:
        delay(1500)

        val fakeUser = UserDto(
            id = UUID.randomUUID().toString(),
            email = "hello@codework.com",
            displayName = "CodeWork Student",
            avatarUrl = "https://example.com/avatar.png",
            isPremium = 1
        )

        val fakeResponse = AuthDto(
            accessToken = "fake_jwt_token_12345",
            user = fakeUser
        )

        return AppResult.Success(fakeResponse)
    }
}
