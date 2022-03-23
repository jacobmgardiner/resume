package com.yoloapps.pitchtrainer

import kotlin.math.abs

data class Attempt(
    /**
     * [0,1] based on closeness to correct answer
     */
    val result: Float,
) {
    companion object {
        fun create(response: Int, note: NoteUtils.NoteLetter): Attempt {
            return Attempt((7 - abs(response - note.ordinal))/7f)
        }
    }

    fun success(): Boolean {
        return result == 1f
    }
}