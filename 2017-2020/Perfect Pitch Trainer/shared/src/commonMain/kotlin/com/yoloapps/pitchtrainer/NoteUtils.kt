package com.yoloapps.pitchtrainer

object NoteUtils {
    enum class NoteLetter {
        C, D, E, F, G, A, B,
    }
    enum class NoteLetterComplete {
        C, CS, D, DS, E, F, FS, G, GS, A, AS, B, BS,
    }

    fun fromInt(note: Int): NoteLetter {
        return NoteLetter.values()[note]
    }
}