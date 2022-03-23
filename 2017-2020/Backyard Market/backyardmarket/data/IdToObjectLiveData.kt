package com.yoloapps.backyardmarket.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.firebase.firestore.*


class IdToObjectLiveData<T>(val context: Context, val viewLifecycleOwner: LifecycleOwner, val queryLiveData: FirebaseQueryLiveData, val type: Class<T>) : LiveData<HashMap<String, T>>() {

    private val repo by lazy { FirestoreRepository.getInstance(context.applicationContext) }

    init {
        value = hashMapOf()
        val idsObserver = Observer<QuerySnapshot> {
            for (change in it.documentChanges) {
                val data = change.document.data.values.toList()
                val uid = change.document.id
                if(data.isNotEmpty() && (data[0] as Boolean)) {
                    when (change.type) {
                        DocumentChange.Type.ADDED -> {
                            addUser(uid)
                        }
                        DocumentChange.Type.MODIFIED -> {
                            addUser(uid)
                        }
                        DocumentChange.Type.REMOVED -> {
                            removeUser(uid)
                        }
                        else -> {

                        }
                    }
                } else { removeUser(uid) }
            }
        }
        queryLiveData.observe(viewLifecycleOwner, idsObserver)
    }

    fun addUser(uid: String) {
        repo.getObjectCache(uid, type)
            .addOnSuccessListener {
                value?.put(uid, it.toObject(type)!!)
                postValue(value)
            }
    }

    fun removeUser(uid: String) {
        value?.remove(uid)
        postValue(value)
    }

    override fun onActive() {
        super.onActive()


    }

    override fun onInactive() {
        super.onInactive()


    }
}