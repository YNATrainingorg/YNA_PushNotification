package com.example.dynamic_audio_player.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

class AudioService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var currentUrl: String? = null
    private var isPrepared = false

    override fun onBind(intent: Intent?): IBinder = binder
    private val binder = AudioBinder()

    inner class AudioBinder : Binder() {
        fun getService(): AudioService = this@AudioService
    }

    fun play(url: String, position: Int = 0) {
        if (url != currentUrl) {
            mediaPlayer?.release()
            mediaPlayer = null
        }

        currentUrl = url
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            setOnPreparedListener {
                it.seekTo(position)
                it.start()
                isPrepared = true
            }
            setOnCompletionListener {
                // Implement callback to Activity/ViewModel if needed
            }
            prepareAsync()
        }
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun resume() {
        mediaPlayer?.start()
    }

    fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
        stopSelf()
    }

    fun getCurrentPosition(): Int = mediaPlayer?.currentPosition ?: 0
    fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}