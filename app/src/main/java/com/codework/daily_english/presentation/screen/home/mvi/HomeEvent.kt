package com.codework.daily_english.presentation.screen.home.mvi

sealed interface HomeEvent {
    data object OnContinueLesson : HomeEvent
    data object OnDailyQuestClick : HomeEvent
    data object OnProfileClick : HomeEvent
    data object OnRetry : HomeEvent
    data object OnQuickActionGemini : HomeEvent
    data object OnQuickActionDictionary : HomeEvent
    data object OnQuickActionGrammar : HomeEvent
    data object OnQuickActionTranslate : HomeEvent
}