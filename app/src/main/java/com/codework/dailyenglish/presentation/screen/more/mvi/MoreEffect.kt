package com.codework.dailyenglish.presentation.screen.more.mvi

import com.codework.dailyenglish.core.base.BaseEffect
import com.codework.dailyenglish.presentation.screen.practice.mvi.PracticeEffect

sealed interface MoreEffect : BaseEffect {
    data object NavigateToEditProfile : MoreEffect
    data object HandleLogout : MoreEffect
    data class NavigateToMenu(val label: String) : MoreEffect
    data object NavigateToLeaderBoard : MoreEffect

}