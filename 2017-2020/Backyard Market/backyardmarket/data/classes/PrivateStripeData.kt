package com.yoloapps.backyardmarket.data.classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PrivateStripeData(
    var state: String? = null,
    var account: Boolean? = null
) : Parcelable {
}