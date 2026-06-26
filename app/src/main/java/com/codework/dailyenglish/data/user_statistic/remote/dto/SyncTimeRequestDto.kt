package com.codework.dailyenglish.data.user_statistic.remote.dto

import com.google.gson.annotations.SerializedName

data class SyncTimeRequestDto(
    @SerializedName("seconds")
    val seconds: Int?
)