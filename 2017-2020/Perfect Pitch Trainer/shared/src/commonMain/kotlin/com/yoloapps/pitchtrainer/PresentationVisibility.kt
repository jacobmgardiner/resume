package com.yoloapps.pitchtrainer

import kotlin.random.Random

data class PresentationVisibility(
    val visible: Boolean,
) {
    override fun equals(other: Any?): Boolean {
        return false
    }

    override fun hashCode(): Int {
        return Random.hashCode()
    }

//    override fun hashCode(): Int {
//        return javaClass.hashCode()
//    }
}