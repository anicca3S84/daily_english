package com.codework.dailyenglish.domain.subscription.model

data class Subscription(
    val id: String,
    val name: String,
    val price: Double,
    val durationDays: Int
)