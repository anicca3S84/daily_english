package com.codework.dailyenglish.data.subscription.remote

import com.codework.dailyenglish.data.remote.response.BaseResponse
import com.codework.dailyenglish.data.subscription.remote.dto.PaymentRequestDto
import com.codework.dailyenglish.data.subscription.remote.dto.PaymentResponseDto
import com.codework.dailyenglish.data.subscription.remote.dto.SubscriptionDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SubscriptionApi {
    @GET("subscriptions")
    suspend fun getSubscriptionPacks(): BaseResponse<List<SubscriptionDto>>

    @POST("subscriptions/pay")
    suspend fun buyPremium(
        @Body request: PaymentRequestDto
    ): BaseResponse<PaymentResponseDto>

    @GET("subscriptions/status/{transactionRef}")
    suspend fun checkPaymentStatus(
        @Path("transactionRef") transactionRef: String
    ): BaseResponse<String>
}