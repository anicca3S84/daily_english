package com.codework.daily_english.presentation.screen.home.mvi

sealed interface HomeEffect {
    data object NavigateToAi : HomeEffect
    data object NavigateToDictionary : HomeEffect
    data object NavigateToGrammar : HomeEffect
    data object NavigateToTranslate : HomeEffect
}