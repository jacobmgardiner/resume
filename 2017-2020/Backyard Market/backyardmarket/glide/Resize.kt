package com.yoloapps.backyardmarket.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.Log
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest
import kotlin.math.max
import kotlin.math.min

class Resize(val center: List<Double>, val radius: Float) : BitmapTransformation() {

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
//        TODO("Not yet implemented")
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val w = /*min(outWidth, toTransform.width)*/outWidth
        val h = /*min(outHeight, toTransform.height)*/outHeight

        val radiusPx = radius * w / 2
        val centerPx = listOf(center[0] * w, center[1] * h)
        val left = centerPx[0] - radiusPx
        val top = centerPx[1] - radiusPx

        Log.d("XXXXXXXXX", "rad: "+radiusPx)
        Log.d("XXXXXXXXX", "center: "+centerPx)
        Log.d("XXXXXXXXX", "W: "+outWidth+", H: "+outHeight)
        Log.d("XXXXXXXXX", "X: "+left+", Y: "+top)

        return Bitmap.createBitmap(toTransform, left.toInt(), top.toInt(), radiusPx.toInt() * 2, radiusPx.toInt() * 2)
    }

}
