package com.yoloapps.reactiontimetracker

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

actual object AsyncTask {
    actual fun run(task: () -> Unit) {
        GlobalScope.launch {
            task()
        }
    }
}