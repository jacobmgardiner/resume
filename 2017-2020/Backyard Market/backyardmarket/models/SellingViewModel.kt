package com.yoloapps.backyardmarket.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.yoloapps.backyardmarket.data.FirebaseQueryLiveData
import com.yoloapps.backyardmarket.data.FirestoreRepository

class SellingViewModel(application: Application) : AndroidViewModel(application) {
    val app: Application = application
    private val repo by lazy { FirestoreRepository.getInstance(app.applicationContext) }

    fun getSelling(user: String): FirebaseQueryLiveData {
        return repo.getSelling(user)
    }
}
