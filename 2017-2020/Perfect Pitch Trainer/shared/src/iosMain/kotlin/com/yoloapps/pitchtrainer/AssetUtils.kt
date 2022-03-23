package com.yoloapps.pitchtrainer

import platform.AVFoundation.AVFileTypeAppleM4A
import platform.AVFoundation.AVFileTypeMPEG4
import platform.Foundation.NSBundle
import platform.Foundation.NSURL
import kotlin.reflect.typeOf

object AssetUtils {
    const val NAME_BUTTON_C = "button_cat"
    const val NAME_BUTTON_D = "button_dog"
    const val NAME_BUTTON_E = "button_elephant"
    const val NAME_BUTTON_F = "button_frog"
    const val NAME_BUTTON_G = "button_giraffe"
    const val NAME_BUTTON_A = "button_alligator"
    const val NAME_BUTTON_B = "button_butterfly"

    const val AUDIO_C = "c"
    const val AUDIO_D = "d"
    const val AUDIO_E = "e"
    const val AUDIO_F = "f"
    const val AUDIO_G = "g"
    const val AUDIO_A = "a"
    const val AUDIO_B = "b"

    const val AUDIO_SUCCEEDED = "succeeded"
    const val AUDIO_FAILED = "failed"

    const val AUDIO_TYPE_MP3 = "mp3"
    const val AUDIO_TYPE_M4A = "m4a"

    fun getButtonName(note: NoteUtils.NoteLetter): String {
        return when(note) {
            NoteUtils.NoteLetter.A -> NAME_BUTTON_A
            NoteUtils.NoteLetter.B -> NAME_BUTTON_B
            NoteUtils.NoteLetter.C -> NAME_BUTTON_C
            NoteUtils.NoteLetter.D -> NAME_BUTTON_D
            NoteUtils.NoteLetter.E -> NAME_BUTTON_E
            NoteUtils.NoteLetter.F -> NAME_BUTTON_F
            NoteUtils.NoteLetter.G -> NAME_BUTTON_G
            else -> NAME_BUTTON_C
        }
    }

    fun getButtonName(noteNumber: Int): String {
        return getButtonName(NoteUtils.NoteLetter.values()[noteNumber])
    }

    fun getButtonUrl(note: NoteUtils.NoteLetter): NSURL {
        return getFileUrl(when(note) {
            NoteUtils.NoteLetter.A -> NAME_BUTTON_A
            NoteUtils.NoteLetter.B -> NAME_BUTTON_B
            NoteUtils.NoteLetter.C -> NAME_BUTTON_C
            NoteUtils.NoteLetter.D -> NAME_BUTTON_D
            NoteUtils.NoteLetter.E -> NAME_BUTTON_E
            NoteUtils.NoteLetter.F -> NAME_BUTTON_F
            NoteUtils.NoteLetter.G -> NAME_BUTTON_G
            else -> NAME_BUTTON_C
        }, AUDIO_TYPE_MP3)
    }

    fun getNoteSoundUrl(note: NoteUtils.NoteLetter): NSURL {
        return getFileUrl(when(note) {
            NoteUtils.NoteLetter.A -> AUDIO_A
            NoteUtils.NoteLetter.B -> AUDIO_B
            NoteUtils.NoteLetter.C -> AUDIO_C
            NoteUtils.NoteLetter.D -> AUDIO_D
            NoteUtils.NoteLetter.E -> AUDIO_E
            NoteUtils.NoteLetter.F -> AUDIO_F
            NoteUtils.NoteLetter.G -> AUDIO_G
            else -> AUDIO_C
        }, AUDIO_TYPE_MP3)
    }

    fun getSucceededSoundUrl(): NSURL {
        return getFileUrl(AUDIO_SUCCEEDED, AUDIO_TYPE_M4A)
    }

    fun getFailedSoundUrl(): NSURL {
        return getFileUrl(AUDIO_FAILED, AUDIO_TYPE_M4A)
    }

    fun getCheck(): String {
        return "check"
    }

    fun getX(): String {
        return "x"
    }

    fun getUnknown(): String {
        return "unknown_presentation"
    }

    fun getBackground(): String {
        return "unknown_presentation"
    }

    fun getFileUrl(filename: String, filetype: String): NSURL {
        println("getting file: $filename")

        return NSURL(fileURLWithPath = NSBundle.mainBundle.pathForResource(name = "audio/$filename", ofType = filetype)!!)
    }
}