package com.yoloapps.backyardmarket.views

import android.content.Context
import android.util.AttributeSet
import com.yoloapps.backyardmarket.data.FirebaseStorageRepository
import com.yoloapps.backyardmarket.data.classes.UserProfile

class ProfileImageView : androidx.appcompat.widget.AppCompatImageView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, /*user: String,*/ size: Int = SIZE_SMALL) : super(context) {
//        this.user = user
        this.size = size
    }

    constructor(context: Context, user: UserProfile, size: Int = SIZE_SMALL) : super(context) {
        this.userProfile = user
        this.size = size
    }

    companion object {
        const val SIZE_SMALL = 0
        const val SIZE_LARGE = 1
    }

    private var size: Int =
        SIZE_SMALL

    private var imageReference: String? = null

    var userProfile: UserProfile? = null
        set(value) {
            field = value
            initView()
        }

    private fun initView() {
        if(userProfile!!.primaryImage != null) {
            imageReference = if (size == SIZE_LARGE) {
                userProfile!!.primaryImage!!
            } else {
                userProfile!!.primaryImage + FirebaseStorageRepository.THUMB
            }

            loadImage()
        }
    }

    private fun loadImage() {
        imageReference?.let { ref ->
            userProfile!!.primaryImageCenter?.let { center ->
                userProfile!!.primaryImageRadius?.let { radius ->
                    FirebaseStorageRepository.loadCircularImage(context, ref, center, radius, this)
                }
            }
        }
    }
}