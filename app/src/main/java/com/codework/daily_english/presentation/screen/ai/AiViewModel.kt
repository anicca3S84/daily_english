package com.codework.daily_english.presentation.screen.ai
import com.codework.daily_english.presentation.model.ChatMessage
import com.codework.daily_english.presentation.model.ChatSession
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codework.daily_english.presentation.components.UiState
import com.codework.daily_english.presentation.navigation.Route
import com.google.ai.client.generativeai.GenerativeModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.withContext


@HiltViewModel
class AiViewModel @Inject constructor(
    private val generativeModel: GenerativeModel // Inject Google AI SDK
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<AiData>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _navigationChannel = Channel<Route>()
    val navigationChannel = _navigationChannel.receiveAsFlow()

    // Biến giữ phiên chat hiện tại để AI nhớ ngữ cảnh
    private var chat = generativeModel.startChat()

    // Mock data lịch sử (Giữ nguyên hoặc load từ Room DB sau này)
    private val mockHistory = listOf(
        ChatSession("1", "Grammar Check", "Is this sentence correct?", "Today"),
    )

    init {
        _uiState.value = UiState.Success(
            AiData(
                messages = emptyList(),
                historySessions = mockHistory,
                currentSessionId = UUID.randomUUID().toString()
            )
        )
    }

    fun onEvent(event: AiEvent) {
        val currentState = (_uiState.value as? UiState.Success)?.data ?: return

        when (event) {
            is AiEvent.OnSendMessage -> sendMessage(event.message)

            is AiEvent.OnNewChat -> {
                // 1. Reset phiên chat của Gemini
                chat = generativeModel.startChat()

                // 2. Reset UI
                _uiState.value = UiState.Success(
                    currentState.copy(
                        messages = emptyList(),
                        currentSessionId = UUID.randomUUID().toString()
                    )
                )
            }

            // ... Các event khác giữ nguyên ...
            is AiEvent.OnLoadSession -> { /* Logic load cũ */ }
            is AiEvent.OnSuggestionClick -> sendMessage(event.text)
            AiEvent.OnImageClick -> { /* Xử lý ảnh (Cần dùng model gemini-pro-vision) */ }
            AiEvent.OnMicClick -> { }
        }
    }

    private fun sendMessage(text: String) {
        if (text.isBlank()) return
        val currentState = (_uiState.value as? UiState.Success)?.data ?: return

        // 1. Cập nhật UI: Hiện tin nhắn User + Hiển thị Typing
        val newMessages = currentState.messages + ChatMessage(text = text, isUser = true)
        _uiState.value = UiState.Success(currentState.copy(messages = newMessages, isTyping = true))

        viewModelScope.launch {
            try {
                // 2. GỌI API GEMINI THẬT
                val response = withContext(Dispatchers.IO) {
                    chat.sendMessage(text)
                }

                // 3. Lấy kết quả text
                val aiReplyText = response.text ?: "Sorry, I didn't understand that."

                // 4. Cập nhật UI: Hiện tin nhắn AI + Tắt Typing
                val updatedMessages = newMessages + ChatMessage(text = aiReplyText, isUser = false)

                _uiState.value = UiState.Success(
                    currentState.copy(
                        messages = updatedMessages,
                        isTyping = false
                    )
                )
            } catch (e: Exception) {
                // 5. Xử lý lỗi (Mất mạng, sai API Key...)
                e.printStackTrace()
                val errorMsg = newMessages + ChatMessage(
                    text = "Error: ${e.localizedMessage ?: "Cannot connect to AI."}",
                    isUser = false
                )
                _uiState.value = UiState.Success(currentState.copy(messages = errorMsg, isTyping = false))
            }
        }
    }
}