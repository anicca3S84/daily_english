package com.codework.dailyenglish.domain.subscription.model

data class PaymentDetails(
    val transactionId: String,
    val transactionRef: String,
    val qrCodeUrl: String,
    val amount: String
)