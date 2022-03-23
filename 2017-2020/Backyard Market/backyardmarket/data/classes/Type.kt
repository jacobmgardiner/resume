package com.yoloapps.backyardmarket.data.classes

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class Type(
        val nameId: String? = null,
        val displayName: String? = null,
        val detailedDisplayName: String? = null,
        val category: Int? = null,
        val defaultAmount: Double? = null,
        val defaultUnit: Int? = null,
        var recommendedPrice: Double? = null,
        val searchTags: List<String>? = null,
        val supportedUnits: List<Int>? = null
    ) : Serializable, Parcelable {

    companion object {
        val NAME_ID = Type::nameId.name
        val DISPLAY_NAME = Type::displayName.name
        val DETAILED_DISPLAY_NAME = Type::detailedDisplayName.name
        val CATEGORY = Type::category.name
        val DEFAULT_AMOUNT = Type::defaultAmount.name
        val DEFAULT_UNIT = Type::defaultUnit.name
        val RECOMMENDED_PRICE = Type::recommendedPrice.name
        val SEARCH_TAGS = Type::searchTags.name
        val SUPPORTED_UNITS = Type::supportedUnits.name

        const val UNIT_ITEM = 0
        const val UNIT_OZ = 1
        const val UNIT_LBS = 2
        const val UNIT_FLOZ = 3
        const val UNIT_PT = 4
        const val UNIT_QT = 5
        const val UNIT_GAL = 6
    }
}