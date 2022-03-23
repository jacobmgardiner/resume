package com.yoloapps.backyardmarket.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.IdToObjectLiveData
import com.yoloapps.backyardmarket.data.classes.UserProfile

class FollowersViewModel(application: Application) : AndroidViewModel(application) {
    val app: Application = application
    private val repo by lazy { FirestoreRepository.getInstance(app.applicationContext) }

    fun getFollowers(viewLifecycleOwner: LifecycleOwner): IdToObjectLiveData<UserProfile> {
        return IdToObjectLiveData(app.applicationContext, viewLifecycleOwner = viewLifecycleOwner, queryLiveData = repo.getFollowersUids(), type = UserProfile::class.java)
    }
}