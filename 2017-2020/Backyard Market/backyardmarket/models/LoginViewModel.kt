package com.yoloapps.backyardmarket.models

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginViewModel(): ViewModel() {
    val TAG = "BM.SignupViewModel"
    var auth = FirebaseAuth.getInstance()

    fun login(activity: Activity, email: String, password: String, success: () -> Unit, failure: (exc: Exception?) -> Unit) {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        success()
                    } else {
                        failure(task.exception)
                    }

                    // ...
                }
        }
    }
}