package com.yoloapps.backyardmarket.models

import android.app.Activity
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.yoloapps.backyardmarket.data.classes.UserProfile
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class VerifyViewModel(): ViewModel() {
    companion object {
        const val TAG = "VerifyViewModel"
        const val COLLECTION_USERS = "users"
    }
    private val auth = FirebaseAuth.getInstance()
    private var done = false

    val isVerified: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun sendEmail(activity: Activity, success: () -> Unit, failure: () -> Unit) {
        auth.currentUser!!.sendEmailVerification()
            .addOnCompleteListener(activity) {
                if (it.isSuccessful) {
                    success()
                } else {
                    failure()
                }
            }

        updateVerification()
    }

    private fun updateVerification() {
        viewModelScope.launch {
            done = false
            while(!done) {
                val user = auth.currentUser
                if (user != null) {
                    user.reload()
                    Log.d(TAG, "Updating verification status: " + user.isEmailVerified)
                    isVerified.value = user.isEmailVerified

                    if(isVerified.value!!) {
                        uploadUserData()
                        done = true
                    }
                }
                delay(1_000)
            }
        }
    }

    private fun uploadUserData() {
        val db = FirebaseFirestore.getInstance()
        val user = auth.currentUser!!
        val profile = UserProfile(
            user.uid,
            user.displayName!!,
            "",
            "NONE",
            user.uid + "/",
            listOf(),
            false
        )
        db.collection(COLLECTION_USERS).document(user.uid)
            .set(profile)
            .addOnCompleteListener {
                done = true
            }
    }
}