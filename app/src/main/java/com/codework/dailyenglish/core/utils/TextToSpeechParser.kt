package com.codework.dailyenglish.core.utils

import android.content.Context
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import android.util.Log
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay




class TextToSpeechParser(context: Context) {
    private val appContext = context.applicationContext
    private var mediaPlayer: MediaPlayer? = null
    private var textToSpeech: TextToSpeech? = null
    private var isTtsReady = false
    private var timeoutJob: Job? = null

    private var pendingText: Triple<String, Float, Locale>? = null

    init {
        initializeTTS()
    }

    private fun initializeTTS() {
        isTtsReady = false
        textToSpeech = TextToSpeech(appContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isTtsReady = true
                Log.d("TTS_UTILS", "TextToSpeech engine đã sẵn sàng.")

                pendingText?.let { (text, speed, locale) ->
                    speakWithTTS(text, speed, locale)
                    pendingText = null
                }
            } else {
                isTtsReady = false
                Log.e("TTS_UTILS", "Khởi tạo TTS thất bại. Status code: $status")
            }
        }
    }

    fun playAudio(
        url: String?,
        textToSpeak: String? = null,
        speed: Float = 1f,
        locale: Locale = Locale.US,
        scope: CoroutineScope
    ) {
        timeoutJob?.cancel()

        // Nếu đối tượng tts đã bị giải phóng (null), tự động khởi tạo lại
        if (textToSpeech == null) {
            initializeTTS()
        }

        if (url.isNullOrBlank()) {
            speakWithTTS(textToSpeak, speed, locale)
            return
        }

        scope.launch(Dispatchers.Main) {
            try {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer()
                } else {
                    mediaPlayer?.setOnPreparedListener(null)
                    mediaPlayer?.setOnErrorListener(null)
                    mediaPlayer?.reset()
                }

                mediaPlayer?.apply {
                    setDataSource(url)
                    setOnPreparedListener { mp ->
                        timeoutJob?.cancel()
                        try {
                            mp.start()
                            if (speed != 1f && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                mp.playbackParams = mp.playbackParams.setSpeed(speed)
                            }
                        } catch (e: Exception) { e.printStackTrace() }
                    }

                    setOnErrorListener { _, what, extra ->
                        timeoutJob?.cancel()
                        speakWithTTS(textToSpeak, speed, locale)
                        true
                    }

                    prepareAsync()

                    timeoutJob = scope.launch(Dispatchers.Main) {
                        delay(2500)
                        try {
                            setOnPreparedListener(null)
                            setOnErrorListener(null)
                            reset()
                        } catch (e: Exception) {}
                        speakWithTTS(textToSpeak, speed, locale)
                    }
                }
            } catch (e: Exception) {
                timeoutJob?.cancel()
                speakWithTTS(textToSpeak, speed, locale)
            }
        }
    }

    private fun speakWithTTS(text: String?, speed: Float, locale: Locale) {
        if (text.isNullOrBlank()) return

        if (!isTtsReady) {
            pendingText = Triple(text, speed, locale)
            return
        }

        try {
            textToSpeech?.apply {
                setSpeechRate(speed)
                val result = setLanguage(locale)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS_UTILS", "Không hỗ trợ ngôn ngữ $locale hoặc kết nối chưa sẵn sàng, thử fallback về English (US)")
                    setLanguage(Locale.US)
                }
                speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun release() {
        try {
            timeoutJob?.cancel()
            mediaPlayer?.release()
            mediaPlayer = null
            textToSpeech?.stop()
            textToSpeech?.shutdown()
            textToSpeech = null  // Gán null để báo hiệu cần khởi tạo lại nếu tái sử dụng
            isTtsReady = false   // Reset trạng thái sẵn sàng
            pendingText = null
        } catch (e: Exception) { e.printStackTrace() }
    }
}