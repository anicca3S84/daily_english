package com.codework.daily_english.data.auth.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthDto(
    @SerializedName("accessToken") val accessToken: String?,
    @SerializedName("refreshToken") val refreshToken: String?,
    @SerializedName("user") val user: UserDto?
)

data class GoogleLoginDto(
    val idToken: String
)

data class UserDto(
    @SerializedName("id") val id: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("fullName") val fullName: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("premium") val isPremium: Boolean?
)