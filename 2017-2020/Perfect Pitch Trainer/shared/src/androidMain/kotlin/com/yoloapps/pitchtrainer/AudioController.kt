package com.yoloapps.pitchtrainer

import android.content.Context
import android.media.*


actual open class AudioController(context: Context, resId: Int, private var listener: AudioManager.AudioEventListener?) {
    private val mediaPlayer: MediaPlayer = MediaPlayer.create(context, resId)

    init {
        mediaPlayer.setOnCompletionListener {
            listener?.onComplete()
//            mediaPlayer.stop()
//            mediaPlayer.release()
            end()
        }
    }

    actual fun play() {
        mediaPlayer.start()
        listener?.onStart()
    }

    actual fun pause() {
        mediaPlayer.pause()
        listener?.onPause()
    }

    actual fun cancel() {
        end()
        listener?.onCancel()
    }

    actual fun end() {
        mediaPlayer.stop()
        mediaPlayer.release()
//        AudioManager.currentAudioController = null
        //TODO("possible leaks?")
        listener?.onEnd()
    }

    actual fun setAudioEventListener(listener: AudioManager.AudioEventListener) {
        this.listener = listener
    }
}