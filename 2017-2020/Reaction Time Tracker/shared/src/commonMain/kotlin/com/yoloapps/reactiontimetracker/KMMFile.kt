package com.yoloapps.reactiontimetracker

expect class KMMFile(parentpath: String, filename: String) {
    val absolutePath: String
    val parent: String

    fun createNewFile()
    fun writeText(text: String)
    fun appendText(text: String)
}