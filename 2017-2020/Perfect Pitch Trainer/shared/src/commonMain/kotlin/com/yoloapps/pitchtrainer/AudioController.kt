package com.yoloapps.pitchtrainer

expect open class AudioController {
    fun play()

    fun pause()

    fun cancel()

    fun end()

    fun setAudioEventListener(listener: AudioManager.AudioEventListener)
}