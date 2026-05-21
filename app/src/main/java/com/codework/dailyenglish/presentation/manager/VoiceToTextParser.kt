package com.codework.dailyenglish.presentation.manager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Quản lý trạng thái ghi âm
data class VoiceToTextState(
    val spokenText: String = "",
    val isSpeaking: Boolean = false,
    val error: String? = null
)

class VoiceToTextParser(private val context: Context) : RecognitionListener {

    private val _state = MutableStateFlow(VoiceToTextState())
    val state: StateFlow<VoiceToTextState> = _state.asStateFlow()

    private var recognizer: SpeechRecognizer? = null

    fun startListening() {
        _state.update { VoiceToTextState() } // Reset trạng thái

        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _state.update { it.copy(error = "Thiết bị không hỗ trợ nhận diện giọng nói") }
            return
        }

        recognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(this@VoiceToTextParser)

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US") // Đặt cứng tiếng Anh
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true) // Cho phép hiển thị text realtime
            }
            startListening(intent)
        }
    }

    fun stopListening() {
        recognizer?.stopListening()
        recognizer?.destroy()
        recognizer = null
        _state.update { it.copy(isSpeaking = false) }
    }

    // Các hàm callback của RecognitionListener
    override fun onReadyForSpeech(params: Bundle?) {
        _state.update { it.copy(isSpeaking = true, error = null) }
    }

    override fun onResults(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (!matches.isNullOrEmpty()) {
            _state.update { it.copy(spokenText = matches.first()) }
        }
        stopListening()
    }

    override fun onPartialResults(partialResults: Bundle?) {
        // Cập nhật UI ngay khi đang nói (realtime)
        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (!matches.isNullOrEmpty()) {
            _state.update { it.copy(spokenText = matches.first()) }
        }
    }

    override fun onError(error: Int) {
        val errorMessage = when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "Lỗi âm thanh"
            SpeechRecognizer.ERROR_NETWORK -> "Lỗi mạng"
            SpeechRecognizer.ERROR_NO_MATCH -> "Không nghe rõ"
            else -> "Đã xảy ra lỗi ($error)"
        }
        _state.update { it.copy(error = errorMessage) }
        stopListening()
    }

    // Bỏ qua các callback không cần thiết
    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(rmsdB: Float) {} // Có thể dùng để vẽ hiệu ứng sóng âm thật
    override fun onBufferReceived(buffer: ByteArray?) {}
    override fun onEndOfSpeech() { _state.update { it.copy(isSpeaking = false) } }
    override fun onEvent(eventType: Int, params: Bundle?) {}
}