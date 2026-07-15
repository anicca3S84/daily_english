package com.codework.dailyenglish.data.auth.remote.dto

import com.google.gson.annotations.SerializedName

data class LogoutRequestDto(
    @SerializedName("refreshToken") val refreshToken: String
)