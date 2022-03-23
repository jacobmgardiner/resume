package com.yoloapps.pitchtrainer

import android.os.Build
import java.io.DataInputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

actual class MultiplatformFile actual constructor(val parentpath: String, val filename: String) {
    actual val parent get() = parentpath
    actual val absolutePath: String
        get() = parent + filename
    private var file: File

    init {
        file = File(parentpath, filename)
    }

    actual val bytes = file.inputStream().readBytes()

    actual fun createNewFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Files.createDirectories(Paths.get(parent))
        } else {
            TODO("fix directory creation on older devices")
        }
        file.createNewFile()
    }

    actual fun writeText(text: String) {
        file.writeText(text)
    }

    actual fun appendText(text: String) {
        file.appendText(text)
    }
}