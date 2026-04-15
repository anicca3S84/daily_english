package com.codework.dailyenglish.data.auth.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.safeApiCall
import com.codework.dailyenglish.core.network.unwrap
import com.codework.dailyenglish.data.auth.remote.AuthApi
import com.codework.dailyenglish.data.auth.remote.dto.AuthDto
import com.codework.dailyenglish.data.auth.remote.dto.GoogleLoginDto
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val api: AuthApi
) : AuthRemoteDataSource {

    override suspend fun loginWithGoogle(idToken: String): AppResult<AuthDto> {
         return safeApiCall { api.loginWithGoogle(GoogleLoginDto(idToken)) }.unwrap()
    }
}
