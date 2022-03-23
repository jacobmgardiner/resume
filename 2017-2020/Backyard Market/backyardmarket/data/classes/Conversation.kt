package com.yoloapps.backyardmarket.data.classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Conversation(
                   val messages: List<Message>? = null,
                   val timeCreated: String? = null,
                   val productId: String? = null,
                   val offerId: Offer? = null,
                   val uids: List<String>? = null
) : Serializable, Parcelable {

    companion object {
        val MESSAGES = Conversation::messages.name
        val TIME = Conversation::timeCreated.name
        val PRODUCT_ID = Conversation::productId.name
        val OFFER_ID = Conversation::offerId.name
        val UIDS = Conversation::uids.name

        const val INDEX_SENDER = 0
        const val INDEX_RECEIVER = 1
    }
}