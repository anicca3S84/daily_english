package com.codework.dailyenglish.data.remote.response

import com.google.gson.annotations.SerializedName

data class  BaseResponse<T>(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: T?
) {
    val isSuccess: Boolean
        get() = status in 200..299
}