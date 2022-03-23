package com.yoloapps.backyardmarket.data.classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class UserProfile(
    val uid: String? = null,
    val displayName: String? = null,
    var description: String? = null,
    var primaryImage: String? = null, // uid/primary/timestamp
    var secondaryImage: String? = null, // uid/secondary/timestamp
    val following: List<String>? = null,
    val featured: Boolean = false,
    val ranking: Int = 0,
    val advertising: Int = 0,
    var permit: Boolean = false,
    val averageRating: Double = 0.0,
    val totalSales: Int = 0,
    val totalProducts: Int = 0,
    val recurringProducts: Int = 0,
    val followers: List<String>? = null,
    var bookmarks: List<String>? = null,
    var primaryImageCenter: List<Double>? = null,
    var primaryImageRadius: Float? = null,
    val otherPoints: Int = 0,
    var lastPrimaryEditTime: String? = null,
    var lastSecondaryEditTime: String? = null
                    ) : Serializable, Parcelable {

    fun newPrimaryReference(editTime: String): String {
            lastPrimaryEditTime = editTime
            return "$uid/primary/${lastPrimaryEditTime}"
        }
    fun newSecondaryReference(editTime: String): String {
        lastSecondaryEditTime = editTime
        return "$uid/secondary/${lastSecondaryEditTime}"
    }

    companion object {
        val UID = UserProfile::uid.name
        val DISPLAY_NAME = UserProfile::displayName.name
        val DESCRIPTION = UserProfile::description.name
        val PRIMARY_IMAGE = UserProfile::primaryImage.name
        val SECONDARY_IMAGE = UserProfile::secondaryImage.name
        val FOLLOWING = UserProfile::following.name
        val FEATURED = UserProfile::featured.name
        val RANKING = UserProfile::ranking.name
        val ADVERTISING = UserProfile::advertising.name
        val PERMIT = UserProfile::permit.name
        val AVERAGE_RATING = UserProfile::averageRating.name
        val TOTAL_SALES = UserProfile::totalSales.name
        val TOTAL_PRODUCTS = UserProfile::totalProducts.name
        val RECURRING_PRODUCTS = UserProfile::recurringProducts.name
        val FOLLOWERS = UserProfile::followers.name
        val OTHER_POINTS = UserProfile::otherPoints.name
    }
}