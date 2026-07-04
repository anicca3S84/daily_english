package com.codework.dailyenglish.core.utils

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class VoiceToTextParserState(
    val spokenText: String = "",
    val isListening: Boolean = false,
    val error: String? = null
)

class VoiceToTextParser(private val app: Application) : RecognitionListener {

    private val _state = MutableStateFlow(VoiceToTextParserState())
    val state = _state.asStateFlow()

    private var recognizer: SpeechRecognizer? = null

    fun startListening(languageCode: String = "en-US") {
        _state.update { VoiceToTextParserState(isListening = true) }

        if (!SpeechRecognizer.isRecognitionAvailable(app)) {
            _state.update {
                it.copy(
                    error = "Speech recognition is not available on this device.",
                    isListening = false
                )
            }
            return
        }

        if (recognizer == null) {
            recognizer = SpeechRecognizer.createSpeechRecognizer(app).apply {
                setRecognitionListener(this@VoiceToTextParser)
            }
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }

        recognizer?.startListening(intent)
    }

    fun stopListening() {
        _state.update { it.copy(isListening = false) }
        recognizer?.stopListening()
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
        _state.update {
            it.copy(
                error = errorMessage,
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