package com.yoloapps.reactiontimetracker

expect class AtomicReferenceKMM<T>(initial: T) {
    var value: T

    fun free()
}