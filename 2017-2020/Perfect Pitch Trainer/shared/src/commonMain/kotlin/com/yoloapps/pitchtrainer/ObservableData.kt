package com.yoloapps.pitchtrainer

class ObservableData<T>(initial: T) {
    var value: T = initial
    set(value) {
        field = value
        onUpdate?.let { it(value) }
    }

    var onUpdate: ((value: T) -> Unit)? = null
}