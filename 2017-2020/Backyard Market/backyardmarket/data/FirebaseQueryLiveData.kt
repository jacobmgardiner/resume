package com.yoloapps.backyardmarket.data

import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*


class FirebaseQueryLiveData(val query: Query) : LiveData<QuerySnapshot>() {
    private val listener = MyValueEventListener()
    private var listenerRegistration: ListenerRegistration? = null

    private var listenerRemovePending = false
    private val handler = Handler()

    private val removeListener = Runnable {
        listenerRegistration!!.remove()
        listenerRemovePending = false
    }

    override fun onActive() {
        super.onActive()

        if (listenerRemovePending) {
            handler.removeCallbacks(removeListener)
        } else {
            listenerRegistration = query.addSnapshotListener(listener)
        }
        listenerRemovePending = false
    }

    override fun onInactive() {
        super.onInactive()

        // Listener removal is schedule on a two second delay
        handler.postDelayed(removeListener, 2000)
        listenerRemovePending = true
    }

    private inner class MyValueEventListener : EventListener<QuerySnapshot?> {
        override fun onEvent(querySnapshot: QuerySnapshot?, e: FirebaseFirestoreException?) {
            if (e != null) {
                Log.e("XXXXXX", "Can't listen to query snapshots: " + querySnapshot + ":::" + e.message)
//                Log.e("XXXXXX", "Original query: " + query)
                return
            }
            this@FirebaseQueryLiveData.value = querySnapshot
        }
    }
}