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


class UserView : CardView {
    companion object {

    }
    var user: UserProfile? = null
        set(value) {
            field = value
            if (value != null) {
                update()
            }
        }

    private lateinit var name: TextView
    private lateinit var description: TextView
    private lateinit var image: ProfileImageView
    private lateinit var container: FrameLayout

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, user: UserProfile, loadOnScroll: Boolean) : super(context) {
        inflate(context, R.layout.user_view, this)
        this.user = user
//        update()
    }

    fun update() {
//        inflate(context, R.layout.user_view, this)

//        container = findViewById(R.id.image_container)
        image = findViewById(R.id.image)
        image.userProfile = user

        name = findViewById(R.id.name)
        name.text = user?.displayName ?: "null"

        description = findViewById(R.id.description)
        description.text = user?.description ?: "null"

        this.setOnClickListener {
            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra(UserActivity.ARG_USER, user as Parcelable)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}