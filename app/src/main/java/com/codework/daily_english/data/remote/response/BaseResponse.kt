package com.codework.daily_english.data.remote.response

import com.google.gson.annotations.SerializedName

data class  BaseResponse<T>(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: T?
) {
    // Check xem status có lọt vào nhóm thành công (200-299) không
    val isSuccess: Boolean
        get() = status in 200..299
}