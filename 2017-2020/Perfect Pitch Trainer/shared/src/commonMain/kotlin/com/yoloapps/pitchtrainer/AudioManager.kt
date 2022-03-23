package com.yoloapps.pitchtrainer

import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object AudioManager {
    enum class PlayMode() {
        replace, append, simultaneous
    }
    var currentAudioController: AudioController? = null
    var playMode = PlayMode.replace

    fun playFile(audioController: AudioController) {
        when(playMode) {
            PlayMode.replace -> {
                currentAudioController?.end()
                currentAudioController = audioController
                audioController.play()
            }
            else -> {
                currentAudioController?.end()
                currentAudioController = audioController
                audioController.play()
            }
        }
    }

    fun playTone() {

    }

    fun pause() {
        currentAudioController?.pause()
    }

    interface AudioEventListener {
        fun onStart()
        fun onPause()
        fun onCancel()
        fun onComplete()
        /**
         * onCancel or onFinish
         */
        fun onEnd()
    }
}