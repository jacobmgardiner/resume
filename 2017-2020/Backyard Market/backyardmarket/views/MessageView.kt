package com.yoloapps.backyardmarket.views

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.classes.Message
import com.yoloapps.backyardmarket.utils.ScreenMeasurementUtils
import com.yoloapps.backyardmarket.utils.TimestampUtils

class MessageView(context: Context) : CardView(context) {
    companion object {
        const val MARGIN_WIDTH = 64
    }
    private lateinit var content: TextView
    private lateinit var time: TextView
    private lateinit var back: CardView
    private lateinit var root: LinearLayout

    var message: Message? = null
        set(value) {
            field = value
            update()
        }

    var isSeller = false

//    constructor(context: Context, message: Message, isSeller: Boolean) : this(context) {
//        inflate(context, R.layout.message_view, this)
//        this.isSeller = isSeller
//        update()
//    }

    private fun update() {
        content = findViewById(R.id.content)
        content.text = message?.content ?: "null"

        back = findViewById(R.id.back)
        root = findViewById(R.id.root)
        time = findViewById(R.id.time)
        time.text = TimestampUtils.timestampToDate(message?.timeSent ?: "null")
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        if(isSeller) {
            if (message!!.seller != null && message!!.seller!!) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    lp.marginStart = ScreenMeasurementUtils.pixelToDp(64, context).toInt()
                } else {
                    lp.leftMargin = ScreenMeasurementUtils.pixelToDp(64, context).toInt()
                }
                back.setCardBackgroundColor(ContextCompat.getColor(context,
                    R.color.colorPrimary
                ))
                root.gravity = Gravity.END
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    lp.marginEnd = ScreenMeasurementUtils.pixelToDp(64, context).toInt()
                } else {
                    lp.rightMargin = ScreenMeasurementUtils.pixelToDp(64, context).toInt()
                }
                back.setCardBackgroundColor(ContextCompat.getColor(context,
                    R.color.colorPrimaryDark
                ))
                root.gravity = Gravity.START
            }
        } else {
            if (message!!.seller != null && message!!.seller!!) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    lp.marginEnd = ScreenMeasurementUtils.pixelToDp(64, context).toInt()
                } else {
                    lp.rightMargin = ScreenMeasurementUtils.pixelToDp(64, context).toInt()
                }
                back.setCardBackgroundColor(ContextCompat.getColor(context,
                    R.color.colorPrimaryDark
                ))
                root.gravity = Gravity.START
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    lp.marginStart = ScreenMeasurementUtils.pixelToDp(64, context).toInt()
                } else {
                    lp.leftMargin = ScreenMeasurementUtils.pixelToDp(64, context).toInt()
                }
                back.setCardBackgroundColor(ContextCompat.getColor(context,
                    R.color.colorPrimary
                ))
                root.gravity = Gravity.END
            }
        }
//        back.layoutParams = lp
    }
}