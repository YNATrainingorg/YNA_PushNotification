package com.example.dynamic_audio_player.viewmodel

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import com.example.dynamic_audio_player.service.AudioService

class PlayerViewModel(application: Application) : AndroidViewModel(application) {

    var audioUrls: List<String> = listOf()
    private var currentIndex = 0
    private var currentPosition = 0
    var isPaused = false

    private var audioService: AudioService? = null

    fun bindService() {
        val context = getApplication<Application>().applicationContext
        val intent = Intent(context, AudioService::class.java)
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            val serviceBinder = binder as AudioService.AudioBinder
            audioService = serviceBinder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            audioService = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        getApplication<Application>().applicationContext.unbindService(serviceConnection)
        audioService = null // 🧹 Cleanup
    }

    fun play() {
        val url = audioUrls[currentIndex]
        audioService?.play(url, currentPosition)
        isPaused = false
    }

    fun pause() {
        audioService?.pause()
        currentPosition = audioService?.getCurrentPosition() ?: 0
        isPaused = true
    }

    fun resume() {
        audioService?.resume()
        isPaused = false
    }

    fun next() {
        currentIndex = (currentIndex + 1) % audioUrls.size
        currentPosition = 0
        play()
    }

    fun previous() {
        currentIndex = if (currentIndex - 1 < 0) audioUrls.size - 1 else currentIndex - 1
        currentPosition = 0
        play()
    }

    // fun isPlaying(): Boolean = audioService?.isPlaying() ?: false
}