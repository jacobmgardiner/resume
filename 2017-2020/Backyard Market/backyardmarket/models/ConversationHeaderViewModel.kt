package com.yoloapps.backyardmarket.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.yoloapps.backyardmarket.data.FirebaseDocumentLiveData
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.classes.Offer

class ConversationHeaderViewModel(application: Application) : AndroidViewModel(application) {
    val app: Application = application
    private val repo by lazy { FirestoreRepository.getInstance(app.applicationContext) }

    fun getIsAccepted(offer: Offer): FirebaseDocumentLiveData {
        return repo.getAcceptedOfferBuyerId(offer)
    }
}