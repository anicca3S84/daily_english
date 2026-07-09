package com.codework.dailyenglish.presentation.screen.subscription.mvi
sealed interface SubscriptionEffect {
    data object NavigateBack : SubscriptionEffect
}