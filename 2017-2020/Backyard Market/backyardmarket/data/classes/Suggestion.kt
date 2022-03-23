package com.yoloapps.backyardmarket.data.classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Suggestion(
    val title: String? = null,
    val content: String? = null,
    val link: String? = null,
    val index: Int? = null
) : Serializable, Parcelable {

    companion object {
        val TITLE = Suggestion::title.name
        val CONTENT = Suggestion::content.name
        val LINK = Suggestion::link.name
        val INDEX = Suggestion::index.name
    }
}