package com.yoloapps.backyardmarket.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.yoloapps.backyardmarket.data.FirebaseDocumentLiveData
import com.yoloapps.backyardmarket.data.FirebaseQueryLiveData
import com.yoloapps.backyardmarket.data.FirestoreRepository

class ProductViewModel(application: Application) : AndroidViewModel(application) {
    val app: Application = application
    private val repo by lazy { FirestoreRepository.getInstance(app.applicationContext) }

    fun getOffers(productId: String): FirebaseQueryLiveData {
        return repo.getOffers(productId)
    }

    fun getBookmarked(productId: String): FirebaseDocumentLiveData {
        return repo.isBookmarkedLive(productId)
    }
}