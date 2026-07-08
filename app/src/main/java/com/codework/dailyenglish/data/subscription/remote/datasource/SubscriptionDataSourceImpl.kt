package com.codework.dailyenglish.data.subscription.remote.datasource

import com.codework.dailyenglish.data.subscription.remote.SubscriptionApi
import com.codework.dailyenglish.data.subscription.remote.dto.PaymentRequestDto
import com.codework.dailyenglish.data.subscription.remote.dto.PaymentResponseDto
import com.codework.dailyenglish.data.subscription.remote.dto.SubscriptionDto
import jakarta.inject.Inject
import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.safeApiCall
import com.codework.dailyenglish.core.network.unwrap


class SubscriptionRemoteDataSourceImpl @Inject constructor(
    private val api: SubscriptionApi
) : SubscriptionRemoteDataSource {

    override suspend fun getSubscriptionPacks(): AppResult<List<SubscriptionDto>> {
        return safeApiCall { api.getSubscriptionPacks() }.unwrap()
    }

    override suspend fun buyPremium(
        subscriptionPublicId: String,
        paymentMethod: String
    ): AppResult<PaymentResponseDto> {
        return safeApiCall {
            api.buyPremium(PaymentRequestDto(subscriptionPublicId, paymentMethod))
        }.unwrap()
    }
}