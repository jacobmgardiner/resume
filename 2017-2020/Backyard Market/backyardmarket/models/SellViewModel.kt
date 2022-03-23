package com.yoloapps.backyardmarket.models

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import com.yoloapps.backyardmarket.data.FirebaseStorageRepository
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.classes.Type
import com.yoloapps.backyardmarket.data.classes.Product
import com.yoloapps.backyardmarket.data.classes.UserProfile
import com.yoloapps.backyardmarket.utils.LocationUtils
import com.yoloapps.backyardmarket.utils.RankingUtils
import com.yoloapps.location.utils.Geohash


class SellViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        const val TAG = "SellViewModel"
    }

    val app: Application = application
    private val repo by lazy { FirestoreRepository.getInstance(app.applicationContext) }
    private val locutils by lazy { LocationUtils.getInstance(app.applicationContext) }

    fun sell(context: Context, activity: Activity, category: Int, type: Type, unit: Int, amount: Int, price: String, totalAmount: Int, title: String, description: String, uid: String, timestamp: String, imageUri: Uri, bitmap: Bitmap, locationPreference: Int, success: () -> Unit, failure: () -> Unit) {
        repo.getUserProfileCache(repo.uid!!)
            .addOnSuccessListener { it ->
                val profile = it.toObject(UserProfile::class.java)
                    locutils.getLocation {
                        val g = Geohash.geohash(it.latitude, it.longitude)
                        val loc = locutils.getLocationAsString(context, Geohash.geohashToCoords(g))
                        val product = Product(
                            productId = repo.getNewProductId(),
                            sellerName = repo.currentUser?.displayName,
                            sellerUid = repo.uid,
                            category = category,
                            type = type,
                            unit = unit,
                            amount = amount,
                            price = price.toDouble(),
                            totalAmount = totalAmount,
                            title = title,
                            description = description,
                            imageReference = null,
                            time = timestamp,
                            g = g,
        //                    featured = profile.featured, TODO ("fix dis")
                            ranking = RankingUtils.calculateRanking(profile!!),
                            city = loc[LocationUtils.Companion.StringLocation.CITY.ordinal],
                            state = loc[LocationUtils.Companion.StringLocation.STATE.ordinal],
                            country = loc[LocationUtils.Companion.StringLocation.COUNTRY.ordinal],
                            county = loc[LocationUtils.Companion.StringLocation.COUNTY.ordinal],
                            postal = loc[LocationUtils.Companion.StringLocation.POSTAL_CODE.ordinal],
                            locationPreference = locationPreference
                        )
                        product.newImageReference(timestamp)

                        FirebaseStorageRepository.uploadImageWithThumbnail(product.imageReference!!, bitmap) {  }

                        repo.uploadProduct(product)
                            .addOnCompleteListener {
                                if (it.isSuccessful)
                                    success()
                                else
                                    failure()
                            }
                    }
        }
    }
}
