package com.codework.daily_english.data.auth.remote.dto

import com.google.gson.annotations.SerializedName


data class AuthDto(
    @SerializedName("access_token") val accessToken: String?,
    @SerializedName("user") val user: UserDto?
)

data class GoogleLoginDto(
    val idToken: String
)

data class UserDto(
    @SerializedName("user_id") val id: String?,
    @SerializedName("email_address") val email: String?,
    @SerializedName("full_name") val displayName: String?,
    @SerializedName("profile_picture") val avatarUrl: String?,
    @SerializedName("premium_status") val isPremium: Int?
)