package com.yoloapps.backyardmarket.utils

import android.content.Context
import android.util.Log
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.classes.Type
import java.text.NumberFormat

object PriceFormatUtils {
    fun toPriceFormat(price: Double): String {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        try {
            return format.format(price)
        } catch (e: IllegalArgumentException) {
            Log.e("XXXXXXX", e.message)
            return "null"
        }
    }
}