package com.yoloapps.backyardmarket.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.GeoPoint
import com.yoloapps.backyardmarket.data.classes.Product
import com.yoloapps.backyardmarket.localpersistence.SharedPreferencesManager

class LocationUtils(val context: Context) {
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: LocationUtils? = null
        fun getInstance(context: Context): LocationUtils {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LocationUtils(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }

        const val SHARED_PREFERENCES_LOCATION = "location"
        const val SHARED_PREFERENCES_LATITUDE = "latitude"
        const val SHARED_PREFERENCES_LONGITUDE = "longitude"

        val DEFAULT_LOCATION = listOf(33.417832F, -111.819503F)
        const val METER_TO_MILE = 0.000621371

        enum class StringLocation {
            POSTAL_CODE, CITY, COUNTY, STATE, COUNTRY
        }
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    init {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        getLocation {  }
    }

    private var location: GeoPoint? = null

    //TODO
    var loc: MutableLiveData<GeoPoint> = MutableLiveData(GeoPoint(DEFAULT_LOCATION[0].toDouble(), DEFAULT_LOCATION[1].toDouble()))

    private val spm by lazy { SharedPreferencesManager.getInstance(context.applicationContext) }

    @SuppressLint("MissingPermission")
    //TODO("fix this nonsense")
    fun getLocation(success: (GeoPoint) -> Unit): GeoPoint? {
        if(location == null) {
            if (!permissionGranted()) return null
            location = spm.loadLocation()
            val task = (fusedLocationClient.lastLocation
                .addOnSuccessListener { l: Location? ->
                    if (l != null) {
                        location = GeoPoint(l.latitude, l.longitude)
                        success(GeoPoint(l.latitude, l.longitude))
                        spm.storeLocation(GeoPoint(l.latitude, l.longitude))
                    } else {

                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
//                    Log.e(TAG, it.message)
                    //failure()
                }
                .addOnCompleteListener {
                })
        } else {
            success(location!!)
        }
        return location
    }



    fun permissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    fun distanceTo(point: GeoPoint, loaded: (Double) -> Unit) {
        getLocation {
            val results = floatArrayOf(0F)
            Location.distanceBetween(
                it.latitude,
                it.longitude,
                point.latitude,
                point.longitude,
                results
            )
            loaded(results[0].toDouble() * METER_TO_MILE)
        }
    }

    fun cleanProductLocation(location: String, type: String): String {
        val loc = location.split(",")
        return when(type) {
            Product.POSTAL -> {
                loc[0]
            }
            Product.CITY -> {
                loc[0] + ", " + loc[1]
            }
            Product.COUNTY -> {
                loc[0] + ", " + loc[1]
            }
            Product.STATE -> {
                loc[0] + ", " + loc[1]
            }
            Product.COUNTRY -> {
                loc[0]
            }
            else -> {
                "error"
            }
        }
    }

    private fun Location.toGeoPoint(): GeoPoint {
        return GeoPoint(this.latitude, this.longitude)
    }

    fun getLocationAsString(context: Context, location: GeoPoint): List<String> {
        val result = Geocoder(context).getFromLocation(location.latitude, location.longitude, 1)
        if (result.isNotEmpty()) {
            val loc = result[0]

            val country = loc.countryName

            val state = loc.adminArea + "," + country

            val county = loc.subAdminArea + "," + state

            val postal = loc.postalCode + "," + country

            val city = loc.locality + "," + state

            return listOf(postal, city, county, state, country)
        } else {
            return listOf()
        }
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        when (requestCode) {
//            PERMISSION_REQUEST_LOCATION -> {
//                // If request is cancelled, the result arrays are empty.
//                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    setupMap()
//                } else {
//                    val intent = Intent(requireContext(), StartActivity::class.java)
//                    startActivity(intent)
//                }
//            }
//            else -> {
//
//            }
//        }
//    }
}