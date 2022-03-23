package com.yoloapps.backyardmarket.models

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.IdToObjectLiveData
import com.yoloapps.backyardmarket.data.classes.Product

class BookmarksViewModel(application: Application) : AndroidViewModel(application) {
    val app: Application = application
    private val repo by lazy { FirestoreRepository.getInstance(app.applicationContext) }

    fun getBookmarks(viewLifecycleOwner: LifecycleOwner): IdToObjectLiveData<Product> {
        return IdToObjectLiveData(context = app.applicationContext, viewLifecycleOwner = viewLifecycleOwner, queryLiveData = repo.getBookmarksIds(), type = Product::class.java)
    }
}


