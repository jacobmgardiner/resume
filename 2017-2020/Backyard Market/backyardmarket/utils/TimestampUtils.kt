package com.yoloapps.backyardmarket.utils

import java.text.SimpleDateFormat
import java.util.*

object TimestampUtils {
    val timestamp: String
        get() {
            return SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").format(Date())
        }

    fun timestampToDate(timestamp: String): String {
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS").parse(timestamp)
        return SimpleDateFormat("E h:mm aa", Locale.US).format(date)
    }
}