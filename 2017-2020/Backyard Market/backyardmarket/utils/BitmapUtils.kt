package com.yoloapps.backyardmarket.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object BitmapUtils {
    private fun getBitmapDescriptor(context: Context, id: Int, wdp: Int, hdp: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, id)
        val h = ScreenMeasurementUtils.dpToPixel(wdp, context)
            .toInt()
        val w = ScreenMeasurementUtils.dpToPixel(hdp, context)
            .toInt()
        vectorDrawable!!.setBounds(0, 0, w, h)
        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bm)
    }
}