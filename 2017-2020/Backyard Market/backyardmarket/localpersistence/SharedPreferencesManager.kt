package com.yoloapps.backyardmarket.localpersistence

import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.firestore.GeoPoint
import com.yoloapps.backyardmarket.utils.LocationUtils
import com.yoloapps.backyardmarket.utils.LocationUtils.Companion.DEFAULT_LOCATION
import com.yoloapps.backyardmarket.utils.NotificationUtils

class SharedPreferencesManager(val context: Context) {
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: SharedPreferencesManager? = null
        fun getInstance(context: Context): SharedPreferencesManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SharedPreferencesManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }

        const val SHARED_PREFERENCES_TOKEN = "token"
        const val SHARED_PREFERENCES_ACCOUNT = "account"

        const val SHARED_PREFERENCES_LOCATION = "location"
        const val SHARED_PREFERENCES_LATITUDE = "latitude"
        const val SHARED_PREFERENCES_LONGITUDE = "longitude"
    }

    fun loadToken(): String? {
        val spl = context.getSharedPreferences(SHARED_PREFERENCES_LOCATION, 0)
        return spl.getString(SHARED_PREFERENCES_TOKEN, null)
    }

    fun storeLocation(point: GeoPoint) {
        val spl = context.getSharedPreferences(LocationUtils.SHARED_PREFERENCES_LOCATION, 0)
        spl.edit()
            .putFloat(SHARED_PREFERENCES_LATITUDE, point.latitude.toFloat())
            .putFloat(SHARED_PREFERENCES_LONGITUDE, point.longitude.toFloat())
            .apply()
    }

    fun loadLocation(): GeoPoint {
        val spl = context.getSharedPreferences(LocationUtils.SHARED_PREFERENCES_LOCATION, 0)
        return GeoPoint(
            spl.getFloat(SHARED_PREFERENCES_LATITUDE, DEFAULT_LOCATION[0]).toDouble(),
            spl.getFloat(SHARED_PREFERENCES_LONGITUDE, DEFAULT_LOCATION[1]).toDouble()
        )
    }
}