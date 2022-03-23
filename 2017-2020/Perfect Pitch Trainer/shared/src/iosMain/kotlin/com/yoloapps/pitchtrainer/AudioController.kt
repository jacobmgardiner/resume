package com.yoloapps.pitchtrainer

import kotlinx.cinterop.*
import objcnames.classes.Protocol
import platform.AVFoundation.*
import platform.Foundation.NSError
import platform.Foundation.NSURL
import platform.darwin.NSObject
import platform.darwin.NSUInteger
import kotlin.reflect.KClass

actual open class AudioController(val fileUrl: NSURL, private var listener: AudioManager.AudioEventListener?) /*: NSObject(), AVAudioPlayerDelegateProtocol*/ {
    private var player: AVAudioPlayer? = null
//    lateinit var player: AVAudioPlayer
//
//    init {
//        player = initPlayer()
//    }

//    private val player: AVAudioPlayer? by lazy {
//        initPlayer()
//    }

//    private val player: AVAudioPlayer = AVAudioPlayer(fileUrl, null).also {
//        initPlayer()
//    }

    @Throws(kotlin.NullPointerException::class)
    private fun initPlayer(): AVAudioPlayer {
//        with(AVAudioSession.sharedInstance()) {
//            setActive(true, null)
//            setCategory(AVAudioSessionCategoryPlayback, AVAudioSessionModeDefault, AVAudioSessionCategoryOptionDefaultToSpeaker, null)
//        }

        println("starting audio player init!!")
        println("fileUrl: $fileUrl")
        try {
            return AVAudioPlayer(fileUrl, null).also {
                println("setting up audio player")
                println("fileUrl: $fileUrl")
                it.delegate = object : NSObject(), AVAudioPlayerDelegateProtocol {
                    override fun audioPlayerDidFinishPlaying(player: AVAudioPlayer, successfully: Boolean) {
                        listener?.onComplete()
                        end()
                    }
                }
                it.prepareToPlay()
            }

        } catch (e: NullPointerException) {
            println("XXXX CAUGHT AUDIO ERROR!!!!")
            e.printStackTrace()
        }

        return AVAudioPlayer()

//        return AVAudioPlayer(fileUrl, null).also {
//            println("setting up audio player")
//            println("fileUrl: $fileUrl")
//            it.delegate = object : NSObject(), AVAudioPlayerDelegateProtocol {
//                override fun audioPlayerDidFinishPlaying(player: AVAudioPlayer, successfully: Boolean) {
//                    listener?.onComplete()
//                    end()
//                }
//            }
//        }
    }

//    @Throws(kotlin.NullPointerException::class)
    actual fun play() {
        with(AVAudioSession.sharedInstance()) {
            setCategory(AVAudioSessionCategoryPlayback, AVAudioSessionModeDefault, AVAudioSessionCategoryOptionDefaultToSpeaker, null)
            setActive(true, null)
        }

        println("starting audio player init!!")
        println("fileUrl: $fileUrl")
        var error: CPointer<ObjCObjectVar<NSError?>>? = null
        try {
            AVAudioPlayer(fileUrl, error).also {
                println("setting up audio player")
                println("fileUrl: $fileUrl")
//                it.delegate = this
                it.delegate = object : NSObject(), AVAudioPlayerDelegateProtocol {
                    override fun audioPlayerDidFinishPlaying(player: AVAudioPlayer, successfully: Boolean) {
                        listener?.onComplete()
                        end()
                    }
                }
                it.prepareToPlay()
            }
                .play()
            listener?.onStart()

        } catch (e: NullPointerException) {
            println("XXXX CAUGHT AUDIO ERROR!!!!")
            println("error: ${error?.pointed?.value?.localizedDescription}")
            e.printStackTrace()
        }

//        player?.play()
//        listener?.onStart()
    }

    actual fun pause() {
        player?.pause()
        listener?.onPause()
    }

    actual fun cancel() {
        end()
        listener?.onCancel()
    }

    actual fun end() {
        player?.stop()
//        player.release()
        //TODO("possible leaks?")
        listener?.onEnd()
    }

    actual fun setAudioEventListener(listener: AudioManager.AudioEventListener) {
        this.listener = listener
    }

//    override fun audioPlayerDidFinishPlaying(player: AVAudioPlayer, successfully: Boolean) {
//        listener?.onComplete()
//        end()
//    }
}