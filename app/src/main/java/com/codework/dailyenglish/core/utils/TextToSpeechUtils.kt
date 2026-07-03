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


class TextToSpeechUtils(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var textToSpeech: TextToSpeech? = null
    private var isTtsReady = false
    private var timeoutJob: Job? = null

    init {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale.US
                isTtsReady = true
            }
        }
    }

    fun playAudio(
        url: String?,
        textToSpeak: String? = null,
        speed: Float = 1f,
        scope: CoroutineScope
    ) {
        // Huỷ bộ đếm timeout cũ nếu có lệnh phát mới
        timeoutJob?.cancel()

        if (url.isNullOrBlank()) {
            speakWithTTS(textToSpeak, speed)
            return
        }

        scope.launch(Dispatchers.Main) {
            try {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer()
                } else {
                    // Xoá listeners cũ để tránh chồng chéo callback khi bấm nhanh
                    mediaPlayer?.setOnPreparedListener(null)
                    mediaPlayer?.setOnErrorListener(null)
                    mediaPlayer?.reset()
                }

                mediaPlayer?.apply {
                    setDataSource(url)

                    setOnPreparedListener { mp ->
                        // Nếu chuẩn bị xong -> Huỷ ngay bộ đếm timeout cứu cánh
                        timeoutJob?.cancel()
                        Log.d("TTS_UTILS", "MediaPlayer chuẩn bị hoàn tất. Bắt đầu phát.")
                        try {
                            mp.start()
                            if (speed != 1f && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                mp.playbackParams = mp.playbackParams.setSpeed(speed)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            try { mp.start() } catch (ex: Exception) {}
                        }
                    }

                    setOnErrorListener { _, what, extra ->
                        timeoutJob?.cancel()
                        Log.e("TTS_UTILS", "MediaPlayer báo lỗi hệ thống: $what, $extra. Chuyển sang TTS.")
                        speakWithTTS(textToSpeak, speed)
                        true
                    }

                    // Kích hoạt chuẩn bị bất đồng bộ
                    prepareAsync()

                    // KÍCH HOẠT CƠ CHẾ CỨU CÁNH (TIMEOUT CHỜ 2.5 GIÂY)
                    // Nếu quá 2.5s mã nguồn native của MediaPlayer vẫn treo ở `makeHTTPConnection`
                    // Luồng này sẽ tự động ngắt Player và ép hệ thống phát giọng nói TTS ngay.
                    timeoutJob = scope.launch(Dispatchers.Main) {
                        delay(2500)
                        Log.w("TTS_UTILS", "URL kết nối quá lâu hoặc bị treo luồng native. Tự động cứu cánh bằng TTS.")
                        try {
                            setOnPreparedListener(null)
                            setOnErrorListener(null)
                            reset() // Giải phóng trạng thái đơ của MediaPlayer
                        } catch (e: Exception) {}
                        speakWithTTS(textToSpeak, speed)
                    }
                }
            } catch (e: Exception) {
                timeoutJob?.cancel()
                e.printStackTrace()
                speakWithTTS(textToSpeak, speed)
            }
        }
    }

    private fun speakWithTTS(text: String?, speed: Float) {
        if (text.isNullOrBlank() || !isTtsReady) return
        try {
            textToSpeech?.apply {
                setSpeechRate(speed)
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}