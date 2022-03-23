package com.yoloapps.backyardmarket.views

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.classes.UserProfile
import com.yoloapps.backyardmarket.activities.UserActivity


class UserViewSmall : CardView {
    companion object {

    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {

    }

    constructor(context: Context) : super(context) {

    }

    var user: UserProfile? = null
        set(value) {
            field = value
            if (value != null) {
                update()
            }
        }

    private lateinit var name: TextView
    private lateinit var image: ProfileImageView
    private lateinit var container: FrameLayout

    constructor(context: Context, user: UserProfile) : super(context) {
        this.user = user
        inflate(context, R.layout.user_view_small, this)
    }

    fun update() {
//        inflate(context, R.layout.user_view_small, this)
//        val img = ProfileImageView(context)
//        container = findViewById(R.id.image_container)
//        container.addView(img)
//        img.userProfile = user
        image = findViewById(R.id.image)
        image.userProfile = user

        name = findViewById(R.id.name)
        name.text = user?.displayName ?: "null"

        this.setOnClickListener {
            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra(UserActivity.ARG_USER, user as Parcelable)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}