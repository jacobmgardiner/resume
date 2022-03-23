package com.yoloapps.backyardmarket.models

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.launch

class SignupViewModel(): ViewModel() {
    val TAG = "BM.SignupViewModel"
    var auth = FirebaseAuth.getInstance()

    fun signup(activity: Activity, first: String, last: String, email: String, password: String, success: () -> Unit, failure: (exc: Exception?) -> Unit) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName("$first $last").build()

                        auth.currentUser!!.updateProfile(profileUpdates)
                            .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                                if (task.isSuccessful) {
                                    Log.d(TAG, "User profile updated.")
                                }
                            })

                        //TODO: create user info

                        success()
                    } else {
                        failure(task.exception)
                    }

                    // ...
                }
        }
    }
}