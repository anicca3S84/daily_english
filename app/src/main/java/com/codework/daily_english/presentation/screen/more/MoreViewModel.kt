package com.codework.daily_english.presentation.screen.more

import androidx.lifecycle.viewModelScope
import com.codework.daily_english.core.base.BaseViewModel
import com.codework.daily_english.core.base.BaseState
import com.codework.daily_english.presentation.screen.more.mvi.MoreEffect
import com.codework.daily_english.presentation.screen.more.mvi.MoreEvent
import com.codework.daily_english.presentation.screen.more.mvi.MoreState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor() : BaseViewModel<MoreState, MoreEvent, MoreEffect>() {

    init {
        loadProfile()
    }

    override fun onEvent(event: MoreEvent) {
        when(event) {
            is MoreEvent.OnEditProfile -> sendEffect(MoreEffect.NavigateToEditProfile)
            is MoreEvent.OnLogout -> sendEffect(MoreEffect.HandleLogout)
            is MoreEvent.OnMenuClick -> sendEffect(MoreEffect.NavigateToMenu(event.label))
            is MoreEvent.OnRetry -> loadProfile()
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { BaseState.Loading }
            delay(1000) // Giả lập mạng

            val mockData = MoreState(
                userName = "CodeWork",
                email = "student@codework.com",
                avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde",
                streak = 15,
                totalXp = 1250,
                wordsLearned = 342,
                appVersion = "1.0.0"
            )

            _uiState.update { BaseState.Success(mockData) }
        }
    }
}