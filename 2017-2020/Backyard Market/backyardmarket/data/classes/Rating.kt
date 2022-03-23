package com.yoloapps.backyardmarket.data.classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Rating(val productId: String? = null,
                 val raterUid: String? = null,
                 val rating: Int? = null,
                 val review: Int? = null,
                 val time: String? = null
) : Parcelable {
}