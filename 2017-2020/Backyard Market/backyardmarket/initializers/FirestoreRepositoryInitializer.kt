package com.yoloapps.backyardmarket.initializers

import android.content.Context
import androidx.startup.Initializer
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.utils.LocationUtils

class FirestoreRepositoryInitializer : Initializer<FirestoreRepository> {
    override fun create(context: Context): FirestoreRepository {

        return FirestoreRepository(context.applicationContext)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        // No dependencies on other libraries.
        return emptyList()
    }
}
