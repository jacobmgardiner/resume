package com.yoloapps.backyardmarket.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.yoloapps.backyardmarket.data.FirebaseStorageRepository
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.classes.Message
import com.yoloapps.backyardmarket.data.classes.Offer


class MessageBarViewModel(application: Application) : AndroidViewModel(application) {
    val app: Application = application
    private val repo by lazy { FirestoreRepository.getInstance(app.applicationContext) }

    fun sendMessage(offer: Offer, message: Message) {
        repo.sendMessage(offer, message)
    }
}
