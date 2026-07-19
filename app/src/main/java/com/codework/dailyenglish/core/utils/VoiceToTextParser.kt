package com.codework.dailyenglish.core.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import com.codework.dailyenglish.R
import com.codework.dailyenglish.presentation.utils.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
// Bổ sung các import cho Coroutine phục vụ đếm thời gian im lặng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class VoiceToTextParserState(
    val spokenText: String = "",
    val isListening: Boolean = false,
    val error: UiText? = null
)

class VoiceToTextParser @Inject constructor() : RecognitionListener {

    private val _state = MutableStateFlow(VoiceToTextParserState())
    val state = _state.asStateFlow()

    private var recognizer: SpeechRecognizer? = null

    // Bộ đếm im lặng thủ công sử dụng Coroutine để giải quyết triệt để độ trễ tiếng Việt
    private val parserScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var silenceJob: Job? = null

    private fun resetSilenceTimer() {
        silenceJob?.cancel()
        silenceJob = parserScope.launch {
            delay(1500L) // Nếu im lặng quá 1.5 giây sau khi bắt đầu nói câu cuối cùng ➔ Tự động dừng mic dịch luôn
            Log.d("VoiceToTextParser", "Phát hiện im lặng 1.5 giây, chủ động dừng ghi âm...")
            stopListening()
        }
    }
    fun reset() {
        silenceJob?.cancel()
        _state.update { VoiceToTextParserState() }
    }


    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val network = connectivityManager?.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }

    private fun getOrCreateRecognizer(context: Context): SpeechRecognizer? {
        if (recognizer == null) {
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

            recognizer = when {
                hasGoogleTtsSpeech -> {
                    Log.d("VoiceToTextParser", "Liên kết trực tiếp dịch vụ Google TTS Service.")
                    SpeechRecognizer.createSpeechRecognizer(context, googleTtsComponent)
                }
                hasGoogleSearchSpeech -> {
                    Log.d("VoiceToTextParser", "Liên kết dịch vụ Google Search Box Speech.")
                    SpeechRecognizer.createSpeechRecognizer(context, googleSearchComponent)
                }
                else -> {
                    Log.d("VoiceToTextParser", "Dùng công cụ mặc định của thiết bị.")
                    SpeechRecognizer.createSpeechRecognizer(context)
                }
            }
            recognizer?.setRecognitionListener(this)
        }
        return recognizer
    }

    fun startListening(
        context: Context,
        languageCode: String = "en-US",
        minDurationMillis: Long = 4000L,
        completeSilenceMillis: Long = 3000L,
        possiblyCompleteSilenceMillis: Long = 2000L
    ) {
        if (!isNetworkAvailable(context)) {
            val networkError = UiText.StringResource(R.string.error_network)
            _state.update {
                VoiceToTextParserState(
                    isListening = false,
                    spokenText = "",
                    error = networkError
                )
            }
            Toast.makeText(context, context.getString(R.string.error_network), Toast.LENGTH_SHORT).show()
            return
        }

        _state.update { VoiceToTextParserState(isListening = true, spokenText = "") }

        val recognizerInstance = getOrCreateRecognizer(context)
        if (recognizerInstance == null) {
            Log.e("VoiceToTextParser", "Thiết bị này không hỗ trợ Speech Recognition.")
            _state.update {
                it.copy(
                    error = UiText.DynamicString("Speech recognition is not available on this device."),
                    isListening = false
                )
            }
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, minDurationMillis)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, completeSilenceMillis)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, possiblyCompleteSilenceMillis)
        }
        try {
            recognizerInstance.startListening(intent)
        } catch (e: Exception) {
            Log.e("VoiceToTextParser", "Lỗi startListening: ${e.message}")
            val err = UiText.DynamicString(e.message.toString())
            _state.update { it.copy(error = err, isListening = false) }
        }
    }

    fun stopListening() {
        silenceJob?.cancel() // Hủy bộ đếm im lặng
        _state.update { it.copy(isListening = false) }
        try {
            recognizer?.stopListening()
        } catch (e: Exception) {
            Log.e("VoiceToTextParser", "Lỗi stopListening: ${e.message}")
            cleanup()
        }
    }

    private fun cleanup() {
        silenceJob?.cancel()
        try {
            recognizer?.destroy()
        } catch (e: Exception) {
            Log.e("VoiceToTextParser", "Lỗi cleanup: ${e.message}")
        } finally {
            recognizer = null
        }
    }

    fun release() {
        cleanup()
    }

    override fun onReadyForSpeech(params: Bundle?) {
        _state.update { it.copy(error = null) }
    }

    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(rmsdB: Float) {}
    override fun onBufferReceived(buffer: ByteArray?) {}

    override fun onEndOfSpeech() {
        // Giữ trạng thái ghi âm cho đến khi kết quả được biên dịch xong hoàn toàn
    }

    override fun onError(error: Int) {
        silenceJob?.cancel()

        if (!_state.value.isListening) {
            Log.d("VoiceToTextParser", "Bỏ qua onError mã $error vì không trong phiên nhận diện.")
            return
        }

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
                isListening = false,
                spokenText = ""
            )
        }
    }

    override fun onResults(results: Bundle?) {
        silenceJob?.cancel() // Hủy bộ đếm im lặng khi đã có kết quả chính thức
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val text = matches?.getOrNull(0) ?: ""
        _state.update {
            it.copy(
                spokenText = text,
                isListening = false
            )
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
            resetSilenceTimer() // Reset bộ đếm im lặng ngay khi có ký tự thô mới được phát âm ra
        }
    }

    override fun onEvent(eventType: Int, params: Bundle?) {}
}