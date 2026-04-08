package com.codework.daily_english.data.auth.remote.datasource

import com.codework.daily_english.core.network.AppResult
import com.codework.daily_english.core.network.safeApiCall
import com.codework.daily_english.core.network.unwrap
import com.codework.daily_english.data.auth.remote.AuthApi
import com.codework.daily_english.data.auth.remote.dto.AuthDto
import com.codework.daily_english.data.auth.remote.dto.GoogleLoginDto
import com.codework.daily_english.data.auth.remote.dto.UserDto
import kotlinx.coroutines.delay
import java.util.UUID
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val api: AuthApi
) : AuthRemoteDataSource {

    override suspend fun loginWithGoogle(idToken: String): AppResult<AuthDto> {
         return safeApiCall { api.loginWithGoogle(GoogleLoginDto(idToken)) }.unwrap()
    }
}
