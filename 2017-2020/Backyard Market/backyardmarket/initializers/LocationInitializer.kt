package com.yoloapps.backyardmarket.initializers

import android.content.Context
import androidx.startup.Initializer
import com.yoloapps.backyardmarket.utils.LocationUtils

class LocationInitializer : Initializer<LocationUtils> {
    override fun create(context: Context): LocationUtils {

        return LocationUtils(context.applicationContext)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        // No dependencies on other libraries.
        return emptyList()
    }
}
