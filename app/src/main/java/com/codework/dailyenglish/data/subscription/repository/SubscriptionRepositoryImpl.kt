package com.codework.dailyenglish.data.subscription.repository

import com.codework.dailyenglish.core.dispatchers.Dispatcher
import com.codework.dailyenglish.core.network.AppResult
import com.codework.dailyenglish.core.network.map
import com.codework.dailyenglish.data.subscription.mapper.toDomain
import com.codework.dailyenglish.data.subscription.remote.datasource.SubscriptionRemoteDataSource
import com.codework.dailyenglish.domain.subscription.model.PaymentDetails
import com.codework.dailyenglish.domain.subscription.model.Subscription
import com.codework.dailyenglish.domain.subscription.repository.SubscriptionRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SubscriptionRepositoryImpl @Inject constructor(
    private val remoteDataSource: SubscriptionRemoteDataSource,
    private val dispatchers: Dispatcher
) : SubscriptionRepository {

    override suspend fun getSubscriptionPacks(): AppResult<List<Subscription>> = withContext(dispatchers.io) {
        remoteDataSource.getSubscriptionPacks().map { listDto ->
            listDto.map { it.toDomain() }
        }
    }

    override suspend fun buyPremium(
        subscriptionPublicId: String,
        paymentMethod: String
    ): AppResult<PaymentDetails> = withContext(dispatchers.io) {
        remoteDataSource.buyPremium(subscriptionPublicId, paymentMethod).map { dto ->
            dto.toDomain()
        }
    }

    override suspend fun checkPaymentStatus(transactionRef: String): AppResult<String> = withContext(dispatchers.io) {
        remoteDataSource.checkPaymentStatus(transactionRef)
    }
}