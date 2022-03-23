package com.yoloapps.backyardmarket.data

import android.content.Context
import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*
import com.yoloapps.backyardmarket.data.FirestoreRepository
import java.security.AccessControlContext


class FirebaseDocumentLiveData(val context: Context, val document: DocumentReference) : LiveData<DocumentSnapshot>() {
    private val listener = MyValueEventListener()
    private var listenerRegistration: ListenerRegistration? = null

    private var listenerRemovePending = false
    private val handler = Handler()

    private val removeListener = Runnable {
        listenerRegistration!!.remove()
        listenerRemovePending = false
    }

    private val repo by lazy { FirestoreRepository.getInstance(context.applicationContext) }

    override fun onActive() {
        super.onActive()

        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener)
        } else {
            listenerRegistration = document.addSnapshotListener(listener)
            repo.callCount()
        }
        listenerRemovePending = false
    }

    override fun onInactive() {
        super.onInactive()

        // Listener removal is schedule on a two second delay
        handler.postDelayed(removeListener, 2000)
        listenerRemovePending = true
    }

    private inner class MyValueEventListener : EventListener<DocumentSnapshot?> {
        override fun onEvent(documentSnapshot: DocumentSnapshot?, e: FirebaseFirestoreException?) {
            if (e != null) {
                Log.e("XXXXXX", "Can't listen to document snapshot: " + documentSnapshot + ":::" + e.message)
                return
            }
            this@FirebaseDocumentLiveData.value = documentSnapshot
        }
    }
}