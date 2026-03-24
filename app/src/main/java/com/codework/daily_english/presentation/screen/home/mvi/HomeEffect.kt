package com.codework.daily_english.presentation.screen.home.mvi

import com.codework.daily_english.core.base.BaseEffect

sealed interface HomeEffect: BaseEffect {
    data object NavigateToAi : HomeEffect
    data object NavigateToDictionary : HomeEffect
    data object NavigateToGrammar : HomeEffect
    data object NavigateToTranslate : HomeEffect
}