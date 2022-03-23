package com.yoloapps.reactiontimetracker

import android.content.Context

actual class FileUtilsFactory(val context: Context) {
    actual fun getInstance(): FileUtils {
        return FileUtils(context)
    }
}