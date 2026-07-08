package com.codework.dailyenglish.data.subscription.mapper

import com.codework.dailyenglish.data.subscription.remote.dto.PaymentResponseDto
import com.codework.dailyenglish.data.subscription.remote.dto.SubscriptionDto
import com.codework.dailyenglish.domain.subscription.model.PaymentDetails
import com.codework.dailyenglish.domain.subscription.model.Subscription

fun SubscriptionDto.toDomain(): Subscription {
    return Subscription(
        id = this.id,
        name = this.name,
        price = this.price,
        durationDays = this.durationDays
    )
}

fun PaymentResponseDto.toDomain(): PaymentDetails {
    return PaymentDetails(
        transactionId = this.transactionId,
        transactionRef = this.transactionRef,
        qrCodeUrl = this.qrCodeUrl,
        amount = this.amount
    )
}