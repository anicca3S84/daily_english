package com.codework.dailyenglish.data.subscription.remote.datasource

import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.data.remote.response.BaseResponse
import com.codework.dailyenglish.data.subscription.remote.dto.PaymentResponseDto
import com.codework.dailyenglish.data.subscription.remote.dto.SubscriptionDto

interface SubscriptionRemoteDataSource {
    suspend fun getSubscriptionPacks(): AppResult<List<SubscriptionDto>>

    suspend fun buyPremium(
        subscriptionPublicId: String,
        paymentMethod: String
    ): AppResult<PaymentResponseDto>

    suspend fun checkPaymentStatus(transactionRef: String): AppResult<String>


}