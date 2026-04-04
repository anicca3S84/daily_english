package com.codework.daily_english.data.auth.remote

import com.codework.daily_english.data.auth.remote.dto.AuthDto
import com.codework.daily_english.data.auth.remote.dto.GoogleLoginDto
import com.codework.daily_english.data.remote.response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/google")
    suspend fun loginWithGoogle(
        @Body request: GoogleLoginDto
    ): BaseResponse<AuthDto>

    @POST("auth/apple")
    suspend fun loginWithApple(
        @Body request: GoogleLoginDto
    ): BaseResponse<AuthDto>

    @POST("auth/email")
    suspend fun loginWithEmail(
        @Body request: GoogleLoginDto
    ): BaseResponse<AuthDto>
}