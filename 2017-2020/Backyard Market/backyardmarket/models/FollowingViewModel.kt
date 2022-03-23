package com.yoloapps.backyardmarket.models

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.QuerySnapshot
import com.yoloapps.backyardmarket.data.FirebaseQueryLiveData
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.IdToObjectLiveData
import com.yoloapps.backyardmarket.data.classes.UserProfile

class FollowingViewModel(application: Application) : AndroidViewModel(application) {
    val app: Application = application
    private val repo by lazy { FirestoreRepository.getInstance(app.applicationContext) }

    fun getFollowing(viewLifecycleOwner: LifecycleOwner): IdToObjectLiveData<UserProfile> {
        return IdToObjectLiveData(app.applicationContext, viewLifecycleOwner, repo.getFollowing(), UserProfile::class.java)
    }
}