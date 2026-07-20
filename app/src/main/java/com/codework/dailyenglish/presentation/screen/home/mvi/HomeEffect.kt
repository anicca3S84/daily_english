package com.codework.dailyenglish.presentation.screen.home.mvi

import com.codework.dailyenglish.core.base.BaseEffect

sealed interface HomeEffect: BaseEffect {
    data object NavigateToAi : HomeEffect
    data object NavigateToDictionary : HomeEffect
    data object NavigateToSavedWord : HomeEffect
    data object NavigateToTranslate : HomeEffect
    data object NavigateToSubscription : HomeEffect
    data object NavigateToProfile : HomeEffect
}