package com.yoloapps.backyardmarket.models

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.yoloapps.backyardmarket.data.FirebaseDocumentLiveData
import com.yoloapps.backyardmarket.data.FirebaseQueryLiveData
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.classes.UserProfile
import com.yoloapps.backyardmarket.data.classes.Product

class UserViewModel(application: Application) : AndroidViewModel(application) {
    val app: Application = application
    private val repo by lazy { FirestoreRepository.getInstance(app.applicationContext) }

    fun getSelling(uid: String): FirebaseQueryLiveData {
        return repo.getSelling(uid)
    }

    fun getFollowing(uid: String): FirebaseDocumentLiveData {
        return repo.getFollowing(uid)
    }
}
