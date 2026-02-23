package com.codework.daily_english.presentation.screen.dictionary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.components.BaseViewModel
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.model.WordItem
import com.codework.daily_english.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel @Inject constructor() : BaseViewModel<DictionaryData>() {

    // Mock Database Từ điển
    private val mockDictionary = listOf(
        WordItem(
            word = "apple",
            phoneticUk = "/ˈæp.əl/",
            phoneticUs = "/ˈæp.əl/",
            type = "noun",
            definition = "A round fruit with red or green skin and a white inside.",
            example = "She ate a fresh apple."
        ),
        WordItem(
            word = "application",
            phoneticUk = "/ˌæp.lɪˈkeɪ.ʃən/",
            phoneticUs = "/ˌæp.ləˈkeɪ.ʃən/",
            type = "noun",
            definition = "A formal request to be considered for a position.",
            example = "I submitted my job application."
        ),

        WordItem(
            word = "application",
            phoneticUk = "/ˌæp.lɪˈkeɪ.ʃən/",
            phoneticUs = "/ˌæp.ləˈkeɪ.ʃən/",
            type = "noun",
            definition = "A formal request to be considered for a position.",
            example = "I submitted my job application."
        ),

        // ... Cập nhật tương tự cho các từ khác ...
    )

    init {
        // Khởi tạo data ban đầu
        _uiState.value = UiState.Success(DictionaryData())
    }

    fun onEvent(event: DictionaryEvent) {
        val currentState = (_uiState.value as? UiState.Success)?.data ?: return

        when (event) {
            is DictionaryEvent.OnSearchQueryChange -> {
                val query = event.query
                // Logic tìm kiếm đơn giản (Search Local)
                val results = if (query.isBlank()) {
                    emptyList()
                } else {
                    mockDictionary.filter { it.word.contains(query, ignoreCase = true) }
                }

                _uiState.value = UiState.Success(
                    currentState.copy(
                        query = query,
                        searchResults = results
                    )
                )
            }

            is DictionaryEvent.OnClearSearch -> {
                _uiState.value = UiState.Success(
                    currentState.copy(query = "", searchResults = emptyList())
                )
            }

            is DictionaryEvent.OnPlayAudio -> {
                // TODO: Tích hợp TextToSpeech (TTS) của Android ở đây
                println("Playing audio for: ${event.word}")
            }

            is DictionaryEvent.OnWordClick -> { /* Navigate to detail screen if needed */ }
        }
    }
}