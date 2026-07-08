package com.codework.dailyenglish.data.subscription.remote.dto

import com.google.gson.annotations.SerializedName

data class SubscriptionDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("price") val price: Double,
    @SerializedName("durationDays") val durationDays: Int
)