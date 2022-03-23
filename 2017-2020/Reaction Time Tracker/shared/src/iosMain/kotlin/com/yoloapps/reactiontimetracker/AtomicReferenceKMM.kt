package com.yoloapps.reactiontimetracker

import kotlinx.atomicfu.atomic

actual class AtomicReferenceKMM<T> actual constructor(private val initial: T) {
    private val _ref = atomic(initial)
    actual var value: T by _ref

//    actual var value: T
//        get() = ref.value
//        set(value) { ref.value = value }

    actual fun free() {

    }
}