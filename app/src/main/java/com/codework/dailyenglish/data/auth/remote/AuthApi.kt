package com.codework.dailyenglish.data.auth.remote

import com.codework.dailyenglish.data.auth.remote.dto.AuthDto
import com.codework.dailyenglish.data.auth.remote.dto.GoogleLoginDto
import com.codework.dailyenglish.data.auth.remote.dto.LogoutRequestDto
import com.codework.dailyenglish.data.auth.remote.dto.UserDto
import com.codework.dailyenglish.data.remote.response.BaseResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part

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
    fun refreshToken(@Header("Authorization") refreshToken: String): Call<BaseResponse<AuthDto>>

    @GET("users/me")
    suspend fun getMyProfile(): BaseResponse<UserDto>

    @Multipart
    @PUT("users/me")
    suspend fun updateMyProfile(
        @Part fullName: MultipartBody.Part, // Thay đổi từ RequestBody sang MultipartBody.Part
        @Part avatar: MultipartBody.Part?
    ): BaseResponse<UserDto>

    @POST("auth/logout")
    suspend fun logout(
        @Body request: LogoutRequestDto
    ): BaseResponse<Unit>
}