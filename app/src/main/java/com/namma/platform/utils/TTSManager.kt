package com.namma.platform.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TTSManager(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isReady = false

    init {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Try Kannada first, fallback to Hindi, then English
            val kannadaResult = tts?.setLanguage(Locale("kn", "IN"))
            if (kannadaResult == TextToSpeech.LANG_MISSING_DATA ||
                kannadaResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                val hindiResult = tts?.setLanguage(Locale("hi", "IN"))
                if (hindiResult == TextToSpeech.LANG_MISSING_DATA ||
                    hindiResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts?.setLanguage(Locale.ENGLISH)
                }
            }
            tts?.setSpeechRate(0.85f)
            tts?.setPitch(1.0f)
            isReady = true
        } else {
            Log.e("TTSManager", "TTS Initialization failed")
        }
    }

    fun speak(text: String) {
        if (isReady) {
            tts?.stop()
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "NammaPlatform_TTS")
        }
    }

    fun speakAnnouncement(
        trainName: String,
        trainNameKannada: String,
        platform: Int,
        destination: String,
        destinationKannada: String
    ) {
        val announcement = buildAnnouncement(
            trainName, trainNameKannada, platform, destination, destinationKannada
        )
        speak(announcement)
    }

    private fun buildAnnouncement(
        trainName: String,
        trainNameKannada: String,
        platform: Int,
        destination: String,
        destinationKannada: String
    ): String {
        // Kannada announcement with English fallback
        return "ಗಮನಿಸಿ! $trainNameKannada, ಪ್ಲಾಟ್‌ಫಾರ್ಮ್ ನಂಬರ್ $platform ರಲ್ಲಿ ಇದೆ. " +
                "$destinationKannada ಗೆ ಹೋಗುತ್ತಿದೆ. " +
                "Attention! $trainName is on Platform $platform, going to $destination."
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isReady = false
    }
}
