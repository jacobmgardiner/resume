package com.yoloapps.backyardmarket.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.google.firebase.firestore.DocumentSnapshot
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.classes.PrivateStripeData
import kotlinx.android.synthetic.main.activity_stripe_setup_completed.*

class StripeSetupCompletedActivity : AppCompatActivity() {
    companion object {
        const val QUERY_PARAM_STATE = "state"
        const val QUERY_PARAM_CODE = "code"
        const val QUERY_PARAM_ERROR = "error"
    }

    private val repo by lazy { FirestoreRepository.getInstance(applicationContext) }

    val state: String? by lazy {
        intent?.data?.getQueryParameter(QUERY_PARAM_STATE)
    }

    val code: String? by lazy {
        intent?.data?.getQueryParameter(QUERY_PARAM_CODE)
    }

    val error: String? by lazy {
        intent?.data?.getQueryParameter(QUERY_PARAM_ERROR)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stripe_setup_completed)

        Log.d("MMMMMMM", "action: ${intent?.action}")
        Log.d("MMMMMMM", "${intent?.data?.query}")

        if (error != null) {
            Log.e("MMMMMMM", error)
            startActivity(Intent(this, MainActivity::class.java))
            return
        }

        Log.d("MMMMMMM", "$state")
        Log.d("MMMMMMM", "$code")

        repo.getSavedRedirectState { savedState ->
            Log.d("MMMMMMM", "states: $state, $savedState")
            if (state == savedState) {
                Log.d("MMMMMMM", "calling: createStripeAccount")
                repo.createStripeAccount(code)
                    .addOnSuccessListener {
                        Log.d("MMMMMMM", "stripe account result: ${it.data.toString()}")
//                        onCreatedUser()
                        val observer = Observer<DocumentSnapshot> { doc ->
                            val acc = doc.toObject(PrivateStripeData::class.java)?.account
                            if(acc != null && acc)
                                onCreatedUser()
                        }
                        repo.getPrivateStripeDataLive().observe(this, observer)
                    }
                    .addOnFailureListener {
                        Log.e("MMMMMMM", "stripe account result: ${it.message}")
                    }
            }
        }
    }

    private fun onCreatedUser() {
        status.text = resources.getString(R.string.success)
        status2.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        returnButton.visibility = View.VISIBLE
        returnButton.setOnClickListener {
            startActivity(Intent(this, SellActivity::class.java))
        }
    }
}