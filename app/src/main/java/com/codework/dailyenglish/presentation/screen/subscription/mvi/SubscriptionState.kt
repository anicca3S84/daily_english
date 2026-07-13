package com.codework.dailyenglish.presentation.screen.subscription.mvi

import com.codework.dailyenglish.core.base.BaseUiState
import com.codework.dailyenglish.domain.subscription.model.PaymentDetails
import com.codework.dailyenglish.domain.subscription.model.Subscription
import com.codework.dailyenglish.presentation.screen.ai.mvi.AiEffect

data class SubscriptionState(
    val packsUiState: BaseUiState<List<Subscription>> = BaseUiState(),
    val paymentUiState: BaseUiState<PaymentDetails> = BaseUiState(),

    val selectedPackId: String = "",
    val isPaymentSuccess: Boolean = false,
    val isPremium: Boolean = false,
    val premiumExpiresAt: String = "",
    val forceShowPacks: Boolean = false,
)