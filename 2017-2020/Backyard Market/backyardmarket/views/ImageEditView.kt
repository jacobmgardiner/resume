package com.yoloapps.backyardmarket.views

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.content.ContextCompat
import com.yoloapps.backyardmarket.R
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt


class ImageEditView (context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    companion object {
        const val BORDER_WIDTH = 20F

        const val BORDER_COLOR = R.color.colorPrimaryDark

        const val MIN_ZOOM = 1F
        const val MAX_ZOOM = 3F
    }

    private var moved = false

    var bitmap: Bitmap? = null
        set(value) {
            field = value
            if(value != null) {
                updateDrawRect()
            }
        }

//    var scale: Float = 1F
//        set(value) {
//            field = value
//            val dimen = (this.dimen * (value)).toInt()
////            val zoomOffset = 0
////
////            drawRect.set(drawRect.left, drawRect.right, dimen, dimen)
//
////            invalidate()
//            Log.d("XXXXXx", "Rect: " + drawRect)
//        }

    val centerX: Double
        get() {
            return drawRect.centerX() / (bitmap?.width ?: -1).toDouble()
        }
    val centerY: Double
        get() {
            return drawRect.centerY() / (bitmap?.height ?: -1).toDouble()
        }
    val radius: Int
        get() {
            Log.d("XXx", "RADIUS::::::::::::::::::::::"+(drawRect.width() / 2) / ((bitmap?.width ?: -1) / 2))
            return (drawRect.width() / 2) / ((bitmap?.width ?: -1) / 2)
        }

    private var back: Bitmap? = null

    private var circlePaint = Paint()
    private var backPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var bitmapPaint = Paint()

    private var lastDragX = -1F
    private var lastDragY = -1F

    private var deltaDragX = -1F
    private var deltaDragY = -1F

    private var lastZoomX = mutableListOf(-1F, -1F)
    private var lastZoomY = mutableListOf(-1F, -1F)

    private var deltaZoomX = mutableListOf(-1F, -1F)
    private var deltaZoomY = mutableListOf(-1F, -1F)

    private var deltaZoom = mutableListOf(-1F, -1F)
    private var deltaZoomAvg = -1

    private var centerZoomX = -1F
    private var centerZoomY = -1F

//    private var startZoomX0 = -1F
//    private var startZoomY0 = -1F
//    private var startZoomX1 = -1F
//    private var startZoomY1 = -1F

//    private var scaleGestureDetector = ScaleGestureDetector(context, ScaleListener{ scale = it })

    private var viewRect: Rect = Rect()
    private var drawRect: Rect = Rect()

    private var dimen: Int = -1

    init {
        circlePaint.strokeWidth = BORDER_WIDTH
        circlePaint.color = ContextCompat.getColor(context, BORDER_COLOR)
        circlePaint.style = Paint.Style.STROKE
    }

    private fun updateDrawRect() {
        bitmap?.width?.let {
            dimen = it.coerceAtMost(bitmap!!.height)
            drawRect.set(drawRect.left, drawRect.right, dimen, dimen)
        }

        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
        viewRect.set(0, 0, measuredHeight, measuredHeight)
        initBack(measuredHeight)
    }

    private fun clamp() {
        val width = bitmap?.width ?: Int.MAX_VALUE
        val height = bitmap?.height ?: Int.MAX_VALUE

        val offsetX = when {
            drawRect.left < 0 -> {
                -drawRect.left
            }
            drawRect.right > width -> {
                width - drawRect.right
            }
            else -> {
                0
            }
        }

        val offsetY = when {
            drawRect.top < 0 -> {
                -drawRect.top
            }
            drawRect.bottom > height -> {
                height - drawRect.bottom
            }
            else -> {
                0
            }
        }

        drawRect.offset(offsetX, offsetY)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        scaleGestureDetector.onTouchEvent(event)
        when (event?.pointerCount) {
            1 -> {
                when (event.action) {
                    MotionEvent.ACTION_MOVE -> {
                        if(event.historySize > 0) {
                            deltaDragX = (lastDragX - event.x) * (drawRect.width() / viewRect.width())
                            deltaDragY = (lastDragY - event.y) * (drawRect.height() / viewRect.height())

                            drawRect.offset(deltaDragX.toInt(), deltaDragY.toInt())

                            clamp()

                            lastDragX = event.x
                            lastDragY = event.y

                            invalidate()
                        }
                    }
                    MotionEvent.ACTION_DOWN -> {
                        lastDragX = event.x
                        lastDragY = event.y
                    }
                    MotionEvent.ACTION_UP -> {
                        Log.d("XXXXXXXX", "RESET!!!!!!!!!!!!!!!!!!!!!!")
                        lastZoomX = mutableListOf(-1F, -1F)
                    }
                }
            }
            2 -> {
                when (event.action) {
                    MotionEvent.ACTION_UP -> {
//                        Log.d("XXXXXXXX", "RESET!!!!!!!!!!!!!!!!!!!!!!")
//                        lastZoomX = mutableListOf(-1F, -1F)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        if(lastZoomX[0].toInt() != -1) {
                            deltaZoomX[0] = event.getX(0) - lastZoomX[0]
                            deltaZoomX[1] = event.getX(1) - lastZoomX[1]
                            deltaZoomY[0] = event.getY(0) - lastZoomY[0]
                            deltaZoomY[1] = event.getY(1) - lastZoomY[1]

                            deltaZoom[0] = sqrt((deltaZoomX[0]).pow(2) + (deltaZoomY[0]).pow(2))
                            deltaZoom[1] = sqrt((deltaZoomX[1]).pow(2) + (deltaZoomY[1]).pow(2))

                            deltaZoomAvg = ((deltaZoom[0] + deltaZoom[1]) / 2).toInt()

                            deltaZoomAvg *= zoomDirection()

                            Log.d("XXXXXXXx", "za: "+deltaZoomAvg)

                            drawRect.inset(deltaZoomAvg, deltaZoomAvg)

//                            clamp()

                            invalidate()
                        }

                        lastZoomX[0] = event.getX(0)
                        lastZoomX[1] = event.getX(1)
                        lastZoomY[0] = event.getY(0)
                        lastZoomY[1] = event.getY(1)
                    }
                }
            }
        }
        return true
    }

    private fun zoomDirection() : Int {
        return if(checkXDir() < 0) {
            if(checkYDir() < 0) {
                -1
            } else {
                0
            }
        } else if(checkXDir() > 0) {
            if(checkYDir() > 0) {
                1
            } else {
                0
            }
        } else {
            0
        }
    }

    private fun checkXDir() : Int {
        return if(lastZoomX[0] > lastZoomX[1]) {
            if(deltaZoomX[0] < 0 && deltaZoomX[1] > 0 ) {
                -1
            } else if(deltaZoomX[1] < 0 && deltaZoomX[0] > 0 ) {
                1
            } else {
                0
            }
        } else {
            if(deltaZoomX[0] < 0 && deltaZoomX[1] > 0 ) {
                1
            } else if(deltaZoomX[1] < 0 && deltaZoomX[0] > 0 ) {
                -1
            } else {
                0
            }
        }
    }

    private fun checkYDir() : Int {
        return if(lastZoomY[0] > lastZoomY[1]) {
            if(deltaZoomY[0] < 0 && deltaZoomY[1] > 0 ) {
                -1
            } else if(deltaZoomY[1] < 0 && deltaZoomY[0] > 0 ) {
                1
            } else {
                0
            }
        } else {
            if(deltaZoomY[0] < 0 && deltaZoomY[1] > 0 ) {
                1
            } else if(deltaZoomY[1] < 0 && deltaZoomY[0] > 0 ) {
                -1
            } else {
                0
            }
        }
    }

    private fun initBack(dimen: Int) {
        back = Bitmap.createBitmap(
            dimen,
            dimen,
            Bitmap.Config.ARGB_8888
        ) // Create a new image we will draw over the

        val backCanvas = Canvas(back!!) // Create a   canvas to draw onto the new image
        backPaint.color = ContextCompat.getColor(context,
            R.color.colorPrimary
        )
        backPaint.alpha = 200
        val outerRectangle = RectF(0F, 0F, dimen.toFloat(), dimen.toFloat())
        backCanvas.drawRect(outerRectangle, backPaint)

        backPaint.color = Color.TRANSPARENT // An obvious color to help debugging
        backPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT) // A out B http://en.wikipedia.org/wiki/File:Alpha_compositing.svg
        val centerX = dimen / 2.toFloat()
        val centerY = dimen / 2.toFloat()
        val radius = dimen / 2F - 5
        backCanvas.drawCircle(centerX, centerY, radius, backPaint)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawColor(Color.BLACK)

        Log.d("XXXXXXXXXXXXX", "dr: "+drawRect+", vr:"+viewRect)
        bitmap?.let { canvas?.drawBitmap(it, drawRect, viewRect, null) }

        back?.let { canvas?.drawBitmap(it, 0F, 0F, null) }

        canvas?.drawCircle(width / 2F, height / 2F, width / 2F - 20, circlePaint)
    }

    private class ScaleListener(val setScale: (Float) -> Unit) : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        var scale = 1F
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            if (detector != null) {
                scale *= detector.scaleFactor
                scale = MIN_ZOOM.coerceAtLeast(min(scale,
                    MAX_ZOOM
                ))
                Log.d("XXXXXXXXX", "scale: "+scale)
                setScale(scale)
            }
            return true
        }
    }
}