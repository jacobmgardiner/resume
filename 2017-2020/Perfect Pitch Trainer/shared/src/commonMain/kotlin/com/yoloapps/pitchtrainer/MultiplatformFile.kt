package com.yoloapps.pitchtrainer

expect class MultiplatformFile(parentpath: String, filename: String) {
    val absolutePath: String
    val parent: String
    val bytes: ByteArray

    fun createNewFile()
    fun writeText(text: String)
    fun appendText(text: String)
}