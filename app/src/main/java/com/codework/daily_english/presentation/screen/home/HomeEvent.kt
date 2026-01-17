package com.codework.daily_english.presentation.screen.home

sealed interface HomeEvent {
    data object OnContinueLesson : HomeEvent
    data object OnDailyQuestClick : HomeEvent
    data object OnProfileClick : HomeEvent
    data object OnRetry : HomeEvent // Thêm event retry khi lỗi mạng
    data object OnQuickActionGemini : HomeEvent
    data object OnQuickActionDictionary : HomeEvent
    data object OnQuickActionGrammar : HomeEvent
    data object OnQuickActionTranslate : HomeEvent
}