package com.yoloapps.backyardmarket.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.yoloapps.backyardmarket.R
import kotlin.math.ceil

object IconUtils {
    fun getIconWithText(context: Context, text: String): BitmapDescriptor {
        val p = Paint()
        p.color = ContextCompat.getColor(context,
            R.color.colorPrimaryDark
        )
        p.typeface = Typeface.DEFAULT_BOLD
        p.textSize = 50f
        val bm = Bitmap.createBitmap(
            ceil(p.measureText(text).toDouble()).toInt(),
            ScreenMeasurementUtils.dpToPixel(25, context).toInt(),
            Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)
        canvas.drawText(text, 0, text.length, 0f, 50f, p)
        return BitmapDescriptorFactory.fromBitmap(bm)
    }

    fun getIconWithNumbers(context: Context, number: Int): BitmapDescriptor? {
        val str = number.toString().padStart(2, '0')
        val first = str[0]
        val second = str[1]
        val back = ContextCompat.getDrawable(context,
            R.drawable.ic_round
        )
        val h = ScreenMeasurementUtils.dpToPixel(
            75,
            context
        ).toInt()
        val w = ScreenMeasurementUtils.dpToPixel(
            75,
            context
        ).toInt()
        back!!.setBounds(0, 0, w, h)

        val h2 = ScreenMeasurementUtils.dpToPixel(
            75,
            context
        ).toInt()
        val w2 = ScreenMeasurementUtils.dpToPixel(
            75,
            context
        ).toInt()

        val firstChar = ContextCompat.getDrawable(context, getResourceIdFromNumber(first))
        firstChar!!.setBounds(0, 0, w2, h2)

        val secondChar = ContextCompat.getDrawable(context, getResourceIdFromNumber(second))
        secondChar!!.setBounds(0, 0, w2, h2)

        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)

        back.draw(canvas)
        if(first != '0') {
            canvas.translate(-30f, 0f)
            firstChar.draw(canvas)
            canvas.translate(60f, 0f)
            secondChar.draw(canvas)
        } else {
            secondChar.draw(canvas)
        }

        return BitmapDescriptorFactory.fromBitmap(bm)
    }

    private fun getIconWithImage(context: Context, resourceId: Int): BitmapDescriptor? {
        val back = ContextCompat.getDrawable(context,
            R.drawable.ic_round
        )
        val h = ScreenMeasurementUtils.dpToPixel(
            40,
            context
        ).toInt()
        val w = ScreenMeasurementUtils.dpToPixel(
            40,
            context
        ).toInt()
        back!!.setBounds(0, 0, w, h)

        val fore = ContextCompat.getDrawable(context, resourceId)
        val h2 = ScreenMeasurementUtils.dpToPixel(
            40,
            context
        ).toInt()
        val w2 = ScreenMeasurementUtils.dpToPixel(
            40,
            context
        ).toInt()
        fore!!.setBounds(0, 0, w2, h2)

        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bm)

        back.draw(canvas)
        fore.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(bm)
    }

    private fun getResourceIdFromNumber(number: Char): Int {
        return when(number) {
            '0' -> R.drawable.ic_number_0
            '1' -> R.drawable.ic_number_1
            '2' -> R.drawable.ic_number_2
            '3' -> R.drawable.ic_number_3
            '4' -> R.drawable.ic_number_4
            '5' -> R.drawable.ic_number_5
            '6' -> R.drawable.ic_number_6
            '7' -> R.drawable.ic_number_7
            '8' -> R.drawable.ic_number_8
            '9' -> R.drawable.ic_number_9
            else -> R.drawable.ic_number_0
        }
    }





//    private fun getIconWithImage(context: Context, resourceId: Int): BitmapDescriptor? {
//        val back = ContextCompat.getDrawable(context,
//            R.drawable.ic_round
//        )
//        val h = ScreenMeasurementUtils.dpToPixel(
//            40,
//            context
//        ).toInt()
//        val w = ScreenMeasurementUtils.dpToPixel(
//            40,
//            context
//        ).toInt()
//        back!!.setBounds(0, 0, w, h)
//
//        val fore = ContextCompat.getDrawable(context, resourceId)
//        val h2 = ScreenMeasurementUtils.dpToPixel(
//            40,
//            context
//        ).toInt()
//        val w2 = ScreenMeasurementUtils.dpToPixel(
//            40,
//            context
//        ).toInt()
//        fore!!.setBounds(0, 0, w2, h2)
//
//        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bm)
//
//        back.draw(canvas)
//        fore.draw(canvas)
//
//        return BitmapDescriptorFactory.fromBitmap(bm)
//    }

//    private fun getIconWithNumbers(context: Context, number: Int): BitmapDescriptor? {
//        val str = number.toString().padStart(2, '0')
//        val first = str[0]
//        val second = str[1]
//        val back = ContextCompat.getDrawable(context,
//            R.drawable.ic_round
//        )
//        val h = ScreenMeasurementUtils.dpToPixel(
//            75,
//            context
//        ).toInt()
//        val w = ScreenMeasurementUtils.dpToPixel(
//            75,
//            context
//        ).toInt()
//        back!!.setBounds(0, 0, w, h)
//
//        val h2 = ScreenMeasurementUtils.dpToPixel(
//            75,
//            context
//        ).toInt()
//        val w2 = ScreenMeasurementUtils.dpToPixel(
//            75,
//            context
//        ).toInt()
//
//        val firstChar = ContextCompat.getDrawable(context, getResourceIdFromNumber(first))
//        firstChar!!.setBounds(0, 0, w2, h2)
//
//        val secondChar = ContextCompat.getDrawable(context, getResourceIdFromNumber(second))
//        secondChar!!.setBounds(0, 0, w2, h2)
//
//        val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bm)
//
//        back.draw(canvas)
//        if(first != '0') {
//            canvas.translate(-30f, 0f)
//            firstChar.draw(canvas)
//            canvas.translate(60f, 0f)
//            secondChar.draw(canvas)
//        } else {
//            secondChar.draw(canvas)
//        }
//
//        return BitmapDescriptorFactory.fromBitmap(bm)
//    }
//
//    private fun getIconWithText(context: Context, text: String): BitmapDescriptor {
//        val p = Paint()
//        p.color = ContextCompat.getColor(context,
//            R.color.colorPrimaryDark
//        )
//        p.typeface = Typeface.DEFAULT_BOLD
//        p.textSize = 50f
//        val bm = Bitmap.createBitmap(
//            ceil(p.measureText(text).toDouble()).toInt(),
//            ScreenMeasurementUtils.dpToPixel(25, context).toInt(),
//            Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bm)
//        canvas.drawText(text, 0, text.length, 0f, 50f, p)
//        return BitmapDescriptorFactory.fromBitmap(bm)
//    }
}