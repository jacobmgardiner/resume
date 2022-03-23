package com.yoloapps.backyardmarket.views

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import com.yoloapps.backyardmarket.R


class DividerLarge(context: Context, val parent: ViewGroup) : View(context) {
    init {
        layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            context.resources.getDimension(R.dimen.divider_large).toInt()
        )
        setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
    }

    val drawable: ShapeDrawable = run {
//        contentDescription = context.resources.getString(R.string.my_view_desc)

        ShapeDrawable(RectShape()).apply {
            // If the color isn't set, the shape uses black as the default.
            paint.color = ContextCompat.getColor(context, R.color.colorPrimary)
            // If the bounds aren't set, the shape can't be drawn.
            setBounds(0, 0, parent.width, context.resources.getDimension(R.dimen.divider_large).toInt())
//            setBounds(0, 0, 99999, 99999)
            Log.d("XXXXXXXXX", "width: "+parent.width+", height: "+context.resources.getDimensionPixelSize(R.dimen.divider_large).toInt())
            this@DividerLarge.invalidate()
        }
    }

//    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
//        super.onLayout(changed, left, top, right, bottom)
//        invalidate()
//    }
//
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//        Log.d("XXXXXx", "DRAWING!!!!")
//        drawable.draw(canvas)
//    }
}
