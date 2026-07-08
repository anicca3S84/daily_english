package com.codework.dailyenglish.data.subscription.remote

import com.codework.dailyenglish.data.remote.response.BaseResponse
import com.codework.dailyenglish.data.subscription.remote.dto.PaymentRequestDto
import com.codework.dailyenglish.data.subscription.remote.dto.PaymentResponseDto
import com.codework.dailyenglish.data.subscription.remote.dto.SubscriptionDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SubscriptionApi {
    @GET("subscriptions")
    suspend fun getSubscriptionPacks(): BaseResponse<List<SubscriptionDto>>

    @POST("subscriptions/pay")
    suspend fun buyPremium(
        @Body request: PaymentRequestDto
    ): BaseResponse<PaymentResponseDto>
}