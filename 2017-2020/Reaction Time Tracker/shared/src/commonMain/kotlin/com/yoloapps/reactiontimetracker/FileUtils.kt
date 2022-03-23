package com.yoloapps.reactiontimetracker

expect class FileUtils {
    val dataFileSaveLocation: String

    fun sendFile(filepath: String/*, uiViewController: Any?*/)
}