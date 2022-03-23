package com.yoloapps.reactiontimetracker

actual class FileUtilsFactory(/**val uiViewController: UIViewController*/) {
    actual fun getInstance(): FileUtils {
        return FileUtils(/**uiViewController*/)
    }
}