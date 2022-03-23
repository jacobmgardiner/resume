package com.yoloapps.backyardmarket.models

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.firestore.FirebaseFirestore
import com.yoloapps.backyardmarket.data.FirebaseQueryLiveData
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.IdToObjectLiveData
import com.yoloapps.backyardmarket.data.classes.Product

class BuyingViewModel(application: Application) : AndroidViewModel(application) {
    val app: Application = application
    private val repo by lazy { FirestoreRepository.getInstance(app.applicationContext) }

    fun getBuying(viewLifecycleOwner: LifecycleOwner): IdToObjectLiveData<Product> {
        return IdToObjectLiveData(app.applicationContext, viewLifecycleOwner, repo.getBuyingUids(), Product::class.java)
    }
}
