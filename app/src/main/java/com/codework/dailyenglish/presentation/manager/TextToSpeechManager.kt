package com.codework.dailyenglish.presentation.manager

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TextToSpeechManager(context: Context) {
    private var tts: TextToSpeech? = null
    private var isReady = false

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.US)
                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    isReady = true
                }
            }
        }
    }

    fun speak(text: String, speed: Float = 1f) {
        if (isReady) {
            tts?.setPitch(1f)
            tts?.setSpeechRate(speed)
            val result = tts?.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "tts_id"
            )
            Log.d("TTS", "Speak result = $result")
        } else {
            Log.d("TTS", "Not ready")
        }
    }

    fun shutDown() {
        tts?.stop()
        tts?.shutdown()
    }
}