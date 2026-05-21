package com.codework.dailyenglish.data.remote.response

import com.google.gson.annotations.SerializedName

data class PageDto<T>(
    @SerializedName("content") val content: List<T>,
    @SerializedName("totalElements") val totalElements: Long,
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("number") val number: Int
)