package com.codework.daily_english.presentation.screen.more
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.components.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<MoreData>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun onEvent(event: MoreEvent) {
        when(event) {
            is MoreEvent.OnEditProfile -> { /* Navigate to Edit Profile */ }
            is MoreEvent.OnLogout -> { /* Handle Logout */ }
            is MoreEvent.OnMenuClick -> { println("Clicked: ${event.label}") }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            delay(1000) // Giả lập mạng

            val mockData = MoreData(
                userName = "CodeWork",
                email = "student@codework.com",
                avatarUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde",
                streak = 15,
                totalXp = 1250,
                wordsLearned = 342
            )

            _uiState.value = UiState.Success(mockData)
        }
    }
}