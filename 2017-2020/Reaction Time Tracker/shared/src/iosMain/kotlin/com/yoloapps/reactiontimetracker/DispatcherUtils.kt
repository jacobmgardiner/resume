package com.yoloapps.reactiontimetracker

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlin.coroutines.CoroutineContext

actual object DispatcherUtils {
    actual fun dispatchOnMain(stuff: () -> Unit) {
        ConcurrenceUtils.dispatchOnMain(object : Runnable {
            override fun run() {
                stuff()
            }
        })
    }

    actual fun dispatchOnOther(stuff: () -> Unit) {
        ConcurrenceUtils.dispatchOnOther(object : Runnable {
            override fun run() {
                stuff()
            }
        })
    }

}