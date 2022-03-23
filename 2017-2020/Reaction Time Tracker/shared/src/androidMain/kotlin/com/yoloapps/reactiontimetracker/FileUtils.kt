package com.yoloapps.reactiontimetracker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

actual class FileUtils(private val context: Context) {
    actual val dataFileSaveLocation get() = "${context.getExternalFilesDir(null)}/reactionTimeData/"

    actual fun sendFile(filepath: String/*, uiViewController: Any?*/) {
        val path: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", File(filepath))
        val emailIntent = Intent(Intent.ACTION_SEND)
        with(emailIntent) {
            type = "vnd.android.cursor.dir/email"
            putExtra(Intent.EXTRA_STREAM, path)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
//            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(Intent.createChooser(emailIntent, "Send data as attachment...")
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}