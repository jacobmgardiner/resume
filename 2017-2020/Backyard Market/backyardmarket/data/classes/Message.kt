package com.yoloapps.backyardmarket.data.classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Message(val senderUid: String? = null,
                   val receiverUid: String? = null,
                   val seller: Boolean? = null,
                   val content: String? = null,
                   val timeSent: String? = null,
                   val productId: String? = null,
                   val uids: List<String>? = null
) : Serializable, Parcelable {

    companion object {
        val SENDER_UID = Message::senderUid.name
        val RECEIVER_UID = Message::receiverUid.name
        val SELLER_NAME = Message::seller.name
        val CONTENT = Message::content.name
        val TIME_SENT = Message::timeSent.name
        val PRODUCT_ID = Message::productId.name
        val UIDS = Message::uids.name

        const val INDEX_SENDER = 0
        const val INDEX_RECEIVER = 1
    }
}