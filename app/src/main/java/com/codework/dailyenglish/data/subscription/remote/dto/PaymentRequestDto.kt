package com.codework.dailyenglish.data.subscription.remote.dto

import com.google.gson.annotations.SerializedName

data class PaymentRequestDto(
    @SerializedName("subscriptionPublicId") val subscriptionPublicId: String,
    @SerializedName("paymentMethod") val paymentMethod: String
)