package com.yoloapps.reactiontimetracker.data

open class ObservableData<T> {
    protected val observers: ArrayList<DataObserver<T>> = ArrayList()
    var data: T? = null
        set(value) {
            if (field == value) return
            field = value
            notifyObservers()
        }

    open fun addObserver(observer: DataObserver<T>) {
        observers.add(observer)
    }

    protected open fun notifyObservers() {
        observers.forEach {
            data?.let { d -> it.onUpdate(d) }
        }
    }
}