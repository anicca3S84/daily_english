package com.codework.dailyenglish.data.subscription.remote.dto

import com.google.gson.annotations.SerializedName

data class PaymentResponseDto(
    @SerializedName("transactionId") val transactionId: String,
    @SerializedName("transactionRef") val transactionRef: String,
    @SerializedName("qrCodeUrl") val qrCodeUrl: String,
    @SerializedName("amount") val amount: String
)