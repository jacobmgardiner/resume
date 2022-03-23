package com.yoloapps.reactiontimetracker

expect object AsyncTask {
    fun run(task: () -> Unit)
}