package com.codework.dailyenglish.core.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener // Thêm import này nếu chưa có
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.utils.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject // Thêm để Hilt tự động nhận diện

data class VoiceToTextParserState(
    val spokenText: String = "",
    val isListening: Boolean = false,
    val error: UiText? = null
)

class VoiceToTextParser @Inject constructor() : RecognitionListener {

    private val _state = MutableStateFlow(VoiceToTextParserState())
    val state = _state.asStateFlow()

    private var recognizer: SpeechRecognizer? = null

    fun startListening(context: Context, languageCode: String = "en-US") {
        _state.update { VoiceToTextParserState(isListening = true, spokenText = "") }

        val googleTtsComponent = ComponentName(
            "com.google.android.tts",
            "com.google.android.apps.speech.tts.googletts.service.GoogleTTSRecognitionService"
        )
        val hasGoogleTtsSpeech = try {
            val intent = Intent("android.speech.RecognitionService").apply {
                component = googleTtsComponent
            }
            context.packageManager.queryIntentServices(intent, 0).isNotEmpty()
        } catch (e: Exception) {
            false
        }

        // 2. Kiểm tra gói ứng dụng "Google Search App" (Ứng dụng Google Tìm kiếm truyền thống)
        val googleSearchComponent = ComponentName(
            "com.google.android.googlequicksearchbox",
            "com.google.android.voicesearch.service.SpeechRecognitionService"
        )
        val hasGoogleSearchSpeech = try {
            val intent = Intent("android.speech.RecognitionService").apply {
                component = googleSearchComponent
            }
            context.packageManager.queryIntentServices(intent, 0).isNotEmpty()
        } catch (e: Exception) {
            false
        }

        // TỐI ƯU HOÁ: Cho phép kích hoạt nếu quét thấy dịch vụ Google thực tế HOẶC hệ thống xác nhận khả dụng
        val isEngineAvailable = hasGoogleTtsSpeech || hasGoogleSearchSpeech || SpeechRecognizer.isRecognitionAvailable(context)

        if (!isEngineAvailable) {
            Log.e("VoiceToTextParser", "Thiết bị này không hỗ trợ Speech Recognition.")
            _state.update {
                it.copy(
                    error = UiText.DynamicString("Speech recognition is not available on this device."),
                    isListening = false
                )
            }
            return
        }

        // Giải phóng kết nối cũ một cách an toàn để tránh xung đột luồng trên Oppo
        try {
            recognizer?.destroy()
        } catch (e: Exception) {
            Log.e("VoiceToTextParser", "Không thể giải phóng recognizer cũ: ${e.message}")
        }

        // Ưu tiên liên kết trực tiếp với gói Google TTS (com.google.android.tts) đã hoạt động trên máy Oppo
        recognizer = when {
            hasGoogleTtsSpeech -> {
                Log.d("VoiceToTextParser", "Liên kết trực tiếp dịch vụ độc lập Google TTS/STT Service.")
                SpeechRecognizer.createSpeechRecognizer(context, googleTtsComponent)
            }
            hasGoogleSearchSpeech -> {
                Log.d("VoiceToTextParser", "Liên kết dịch vụ Google Search Box Speech.")
                SpeechRecognizer.createSpeechRecognizer(context, googleSearchComponent)
            }
            else -> {
                Log.d("VoiceToTextParser", "Không tìm thấy Engine Google, dùng mặc định của thiết bị.")
                SpeechRecognizer.createSpeechRecognizer(context)
            }
        }

        recognizer?.setRecognitionListener(this)

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)

            // TỐI ƯU HÓA: Cấu hình thêm thời gian chờ im lặng để tránh ngắt mic quá sớm
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 3000L) // Giữ mic mở ít nhất 3 giây
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 2000L) // Chờ 2 giây im lặng hoàn toàn trước khi ngắt kết nối
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 1500L) // Chờ 1.5 giây nghỉ ngơi giữa các từ
        }

        try {
            recognizer?.startListening(intent)
        } catch (e: Exception) {
            Log.e("VoiceToTextParser", "Lỗi startListening: ${e.message}")
            val err = UiText.DynamicString(e.message.toString())
            _state.update { it.copy(error = err, isListening = false) }
        }
    }

    fun stopListening() {
        _state.update { it.copy(isListening = false) }
        try {
            recognizer?.stopListening()
        } catch (e: Exception) {
            Log.e("VoiceToTextParser", "Lỗi stopListening: ${e.message}")
        } finally {
            recognizer?.destroy()
            recognizer = null
        }
    }

    fun release() {
        recognizer?.destroy()
        recognizer = null
    }

    override fun onReadyForSpeech(params: Bundle?) {
        _state.update { it.copy(error = null) }
    }

    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(rmsdB: Float) {}
    override fun onBufferReceived(buffer: ByteArray?) {}

    override fun onEndOfSpeech() {
        _state.update { it.copy(isListening = false) }
    }

    override fun onError(error: Int) {
        val errorMessage = when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> "Client side error"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
            SpeechRecognizer.ERROR_NETWORK -> "Network error"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
            SpeechRecognizer.ERROR_NO_MATCH -> "No match found"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Speech service is busy"
            SpeechRecognizer.ERROR_SERVER -> "Server error"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
            else -> "Unknown error"
        }
        Log.e("VoiceToTextParser", "onError: Mã lỗi hệ thống: $error ($errorMessage)")
        _state.update {
            val err = UiText.DynamicString(errorMessage)
            it.copy(
                error = err,
                isListening = false
            )
        }
    }

    override fun onResults(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val text = matches?.getOrNull(0) ?: ""
        if (text.isNotBlank()) {
            _state.update { it.copy(spokenText = text, isListening = false) }
        }
    }

    override fun onPartialResults(partialResults: Bundle?) {
        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val unstableMatches = partialResults?.getStringArrayList("android.speech.extra.UNSTABLE_TEXT")

        var completeText = matches?.getOrNull(0) ?: ""
        val unstableText = unstableMatches?.getOrNull(0) ?: ""

        if (unstableText.isNotEmpty()) {
            completeText = if (completeText.isNotEmpty()) "$completeText $unstableText" else unstableText
        }

        if (completeText.isNotBlank()) {
            _state.update { it.copy(spokenText = completeText) }
        }
    }

    override fun onEvent(eventType: Int, params: Bundle?) {}
}