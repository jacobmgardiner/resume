package com.yoloapps.pitchtrainer.android

import com.yoloapps.pitchtrainer.NoteUtils

object ResourceIdMapper {
    fun audioFile(note: NoteUtils.NoteLetter): Int {
        return when(note) {
            NoteUtils.NoteLetter.C -> { R.raw.c }
//            NoteUtils.NoteLetter.CS -> { R.raw.csh }
            NoteUtils.NoteLetter.D -> { R.raw.d }
//            NoteUtils.NoteLetter.DS -> { R.raw.dsh }
            NoteUtils.NoteLetter.E -> { R.raw.e }
            NoteUtils.NoteLetter.F -> { R.raw.f }
//            NoteUtils.NoteLetter.FS -> { R.raw.fsh }
            NoteUtils.NoteLetter.G -> { R.raw.g }
//            NoteUtils.NoteLetter.GS -> { R.raw.gsh }
            NoteUtils.NoteLetter.A -> { R.raw.a }
//            NoteUtils.NoteLetter.AS -> { R.raw.ash }
            NoteUtils.NoteLetter.B -> { R.raw.b }
//            NoteUtils.NoteLetter.BS -> { R.raw.bsh }
            else -> { R.raw.test }
        }
    }

    fun drawable(note: NoteUtils.NoteLetter): Int {
        return when(note) {
            NoteUtils.NoteLetter.C -> { R.raw.c }
//            NoteUtils.NoteLetter.CS -> { R.raw.csh }
            NoteUtils.NoteLetter.D -> { R.raw.d }
//            NoteUtils.NoteLetter.DS -> { R.raw.dsh }
            NoteUtils.NoteLetter.E -> { R.raw.e }
            NoteUtils.NoteLetter.F -> { R.raw.f }
//            NoteUtils.NoteLetter.FS -> { R.raw.fsh }
            NoteUtils.NoteLetter.G -> { R.raw.g }
//            NoteUtils.NoteLetter.GS -> { R.raw.gsh }
            NoteUtils.NoteLetter.A -> { R.raw.a }
//            NoteUtils.NoteLetter.AS -> { R.raw.ash }
            NoteUtils.NoteLetter.B -> { R.raw.b }
//            NoteUtils.NoteLetter.BS -> { R.raw.bsh }
            else -> { R.raw.test }
        }
    }

    fun buttonFromInt(button: Int): Int {
        return when(button) {
            0 -> { R.drawable.button_cat }
            1 -> { R.drawable.button_dog }
            2 -> { R.drawable.button_elephant }
            3 -> { R.drawable.button_frog }
            4 -> { R.drawable.button_giraffe }
            5 -> { R.drawable.button_alligator }
            6 -> { R.drawable.button_butterfly }
            else -> { R.drawable.button_cat }
        }
    }
}