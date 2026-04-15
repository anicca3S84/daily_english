package com.codework.dailyenglish.data.auth.remote

import com.codework.dailyenglish.data.auth.remote.dto.AuthDto
import com.codework.dailyenglish.data.auth.remote.dto.GoogleLoginDto
import com.codework.dailyenglish.data.remote.response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.Call

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

    @POST("auth/refresh")
    fun refreshToken(@Header("Authorization") refreshToken: String): Call<AuthDto>
}