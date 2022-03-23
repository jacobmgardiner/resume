package com.yoloapps.reactiontimetracker.data

abstract class DataObserver<T> {
    abstract fun onUpdate(data: T)
}