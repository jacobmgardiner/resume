package com.yoloapps.backyardmarket.data.classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val productId: String? = null,
    val sellerName: String? = null,
    val sellerUid: String? = null,
    val category: Int? = null,
    val type: Type? = null,
    val unit: Int? = null,
    val amount: Int? = null,
    val price: Double? = null,
    val totalAmount: Int? = null,
    val title: String? = null,
    val description: String? = null,
    var imageReference: String? = null, //sellerUid/productId
    var time: String? = null,
    var g: String? = null,
    var featured: Boolean? = false,
    var ranking: Int? = null,
    var city: String? = null,
    var state: String? = null,
    var country: String? = null,
    var county: String? = null,
    var postal: String? = null,
    var lastEditTime: String? = null,
    var locationPreference: Int? = null
) : Parcelable {

    fun newImageReference(editTime: String?) {
        lastEditTime = editTime
        imageReference = "$sellerUid/$productId/$lastEditTime"
    }

    companion object {
        val PRODUCT_ID = Product::productId.name
        val SELLER_NAME = Product::sellerName.name
        const val SELLER_UID = "sellerUid"
        const val CATEGORY = "category"
        val TYPE = Product::type.name
        val UNIT = Product::unit.name
        val AMOUNT = Product::unit.name
        val PRICE = Product::price.name
        val TOTAL_AMOUNT = Product::price.name
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val IMAGE_REFERENCE = "imageReference"
        const val TIME = "time"
        val GEOHASH = Product::g.name
        val FEATURED = Product::featured.name
        val RANKING = Product::ranking.name
        val CITY = Product::city.name
        val STATE = Product::state.name
        val COUNTRY = Product::country.name
        val COUNTY = Product::county.name
        val POSTAL = Product::postal.name
        val LAST_EDIT_TIME = Product::lastEditTime.name
        val LOCATION_PREFERENCES = Product::locationPreference.name

        const val CATEGORY_DAIRY = 0
        const val CATEGORY_VEGETABLES = 1
        const val CATEGORY_FRUIT = 2
        const val CATEGORY_PLANTS = 3
        const val CATEGORY_BEES = 4
        const val CATEGORY_OTHER = 5
    }
}