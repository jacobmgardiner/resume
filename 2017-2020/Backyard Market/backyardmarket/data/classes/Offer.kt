package com.yoloapps.backyardmarket.data.classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Offer(val productId: String? = null,
                 val buyerUid: String? = null,
                 val sellerUid: String? = null,
                 val amount: Int? = null,
                 val unit: Int? = null,
                 val price: Double? = null,
                 val time: String? = null
) : Parcelable {
}