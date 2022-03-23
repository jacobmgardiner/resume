package com.yoloapps.backyardmarket.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.localpersistence.SharedPreferencesManager
import javax.inject.Singleton




class NotificationUtils(val context: Context) {
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: NotificationUtils? = null
        fun getInstance(context: Context): NotificationUtils {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: NotificationUtils(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }

    private val repo by lazy { FirestoreRepository.getInstance(context.applicationContext) }
    private val spm by lazy { SharedPreferencesManager.getInstance(context.applicationContext) }

    init {
        if(spm.loadToken() == null)
            uploadToken()
    }

    private fun uploadToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(
                OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("MMMMMMM", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token
                    if (token != null) {
                        repo.uploadNotificationToken(token)
                    }
                }
            )
    }
}