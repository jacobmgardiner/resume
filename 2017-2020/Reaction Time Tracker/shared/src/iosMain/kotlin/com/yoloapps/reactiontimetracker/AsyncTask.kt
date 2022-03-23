package com.yoloapps.reactiontimetracker

import platform.darwin.*
import platform.posix.intptr_t

actual object AsyncTask {
    actual fun run(task: () -> Unit) {
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT as intptr_t, 0)) {
            task()
        }
    }
}