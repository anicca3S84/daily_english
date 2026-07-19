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
// BỔ SUNG CÁC IMPORT PHỤC VỤ CHECK MẠNG VÀ HIỂN THỊ TOAST
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import com.codework.dailyenglish.R

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

    // Hàm Helper kiểm tra trạng thái Internet
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val network = connectivityManager?.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
    }

    private fun initializeTTS() {
        isTtsReady = false
        val engines = try {
            TextToSpeech(appContext) {}.engines
        } catch (e: Exception) {
            emptyList()
        }

        val hasGoogleTts = engines.any { it.name == "com.google.android.tts" }

        if (hasGoogleTts) {
            Log.d("TTS_UTILS", "Google TTS khả dụng. Khởi tạo bằng Google TTS.")
            textToSpeech = try {
                TextToSpeech(appContext, { status ->
                    if (status == TextToSpeech.SUCCESS) {
                        isTtsReady = true
                        pendingText?.let { (text, speed, locale) ->
                            speakWithTTS(text, speed, locale)
                            pendingText = null
                        }
                    } else {
                        Log.w("TTS_UTILS", "Khởi tạo Google TTS không thành công. Chuyển sang mặc định.")
                        createDefaultTTS()
                    }
                }, "com.google.android.tts")
            } catch (e: Exception) {
                createDefaultTTS()
                null
            }
        } else {
            Log.d("TTS_UTILS", "Không tìm thấy Google TTS. Khởi tạo mặc định hệ thống (Oppo/Samsung OEM).")
            createDefaultTTS()
        }
    }

    private fun createDefaultTTS() {
        try {
            textToSpeech?.shutdown()
        } catch (e: Exception) {}

        textToSpeech = TextToSpeech(appContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isTtsReady = true
                Log.d("TTS_UTILS", "Default TTS sẵn sàng.")
                pendingText?.let { (text, speed, locale) ->
                    speakWithTTS(text, speed, locale)
                    pendingText = null
                }
            } else {
                isTtsReady = false
                Log.e("TTS_UTILS", "Khởi tạo default TTS thất bại. Mã lỗi: $status")
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
        // TỰ ĐỘNG CHECK MẠNG: Nếu mất mạng, hiện Toast và dừng luôn không chạy tiếp
        if (!isNetworkAvailable()) {
            Toast.makeText(
                appContext,
                appContext.getString(R.string.error_network),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        timeoutJob?.cancel()
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
            textToSpeech = null
            isTtsReady = false
            pendingText = null
        } catch (e: Exception) { e.printStackTrace() }
    }
}