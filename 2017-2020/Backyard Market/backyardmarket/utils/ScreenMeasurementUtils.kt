package com.yoloapps.backyardmarket.utils

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue

object ScreenMeasurementUtils {
    fun dpToPixel(dp: Int, context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            context.resources.displayMetrics
        )
    }

    fun pixelToDp(dp: Int, context: Context): Float {
        return Resources.getSystem().displayMetrics.density * dp
    }
}