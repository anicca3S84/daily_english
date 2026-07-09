package com.codework.dailyenglish.presentation.screen.subscription.mvi

sealed interface SubscriptionEvent {
    data object OnInitLoad : SubscriptionEvent
    data class OnSelectPack(val packId: String) : SubscriptionEvent
    data object OnBuyClick : SubscriptionEvent
    data object OnDismissBottomSheet : SubscriptionEvent
    data object OnPaymentSuccessMock : SubscriptionEvent
    data object OnBackClick : SubscriptionEvent
    data object OnShowPacksClick : SubscriptionEvent
}