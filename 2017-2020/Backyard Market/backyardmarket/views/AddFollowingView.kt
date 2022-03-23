package com.yoloapps.backyardmarket.views

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.classes.UserProfile
import com.yoloapps.backyardmarket.activities.UserActivity


class AddFollowingView : CardView {
    companion object {

    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {

    }

    private lateinit var name: TextView
    private lateinit var image: ImageView
    private lateinit var container: FrameLayout

    constructor(context: Context) : super(context) {
        inflate(context, R.layout.user_view_small, this)
    }

    fun update() {
//        inflate(context, R.layout.user_view_small, this)
//        val img = ProfileImageView(context)
//        container = findViewById(R.id.image_container)
//        container.addView(img)
//        img.userProfile = user
        image = findViewById(R.id.image)
//        image.userProfile = user

        name = findViewById(R.id.name)
        name.text = "Follow Someone New"

        this.setOnClickListener {
//            val intent = Intent(context, UserActivity::class.java)
//            intent.putExtra(UserActivity.ARG_USER, user as Parcelable)
//
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            context.startActivity(intent)
        }
    }
}