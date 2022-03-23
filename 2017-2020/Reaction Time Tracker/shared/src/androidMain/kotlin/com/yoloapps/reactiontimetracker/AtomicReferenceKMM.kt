package com.yoloapps.reactiontimetracker

actual class AtomicReferenceKMM<T>actual constructor(private val initial: T){
    actual var value: T = initial

    actual fun free() {
    }
}