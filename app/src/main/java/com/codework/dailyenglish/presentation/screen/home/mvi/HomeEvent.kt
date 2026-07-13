package com.codework.dailyenglish.presentation.screen.home.mvi

sealed interface HomeEvent {
    data object OnContinueLesson : HomeEvent
    data object OnDailyQuestClick : HomeEvent
    data object OnProfileClick : HomeEvent
    data object OnRetry : HomeEvent
    data object OnQuickActionGemini : HomeEvent
    data object OnQuickActionDictionary : HomeEvent
    data object OnQuickActionSavedWord : HomeEvent
    data object OnQuickActionTranslate : HomeEvent
    data object OnPremiumCrownClick : HomeEvent
    data object OnDismissSubscriptionPopup : HomeEvent
    data object OnEnterHomeScreen : HomeEvent

}