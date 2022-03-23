package com.yoloapps.backyardmarket.activities

import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.text.bold
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.models.VerifyViewModel
import kotlinx.android.synthetic.main.activity_sell.*


class VerifyActivity : AppCompatActivity() {
    lateinit var  model: VerifyViewModel
    lateinit var  coordinator: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)

        coordinator = findViewById(R.id.coordinator)

        model = ViewModelProviders.of(this).get(VerifyViewModel::class.java)
        model.sendEmail(this, {
//            Toast.makeText(this,
//                "Sent verification email.",
//                Toast.LENGTH_SHORT).show()
            findViewById<TextView>(R.id.status).text = SpannableStringBuilder()
                .append("Sent email to \n")
                .bold { append(FirebaseAuth.getInstance().currentUser!!.email) }
        }, {
//            Toast.makeText(this,
//                "Failed to send verification email.",
//                Toast.LENGTH_SHORT).show()
            Snackbar.make(coordinator, "Failed to send verification email", Snackbar.LENGTH_LONG).setAction("Retry") { model.sendEmail(this, {
                findViewById<TextView>(R.id.status).text = SpannableStringBuilder()
                    .append("Sent email to \n")
                    .bold { append(FirebaseAuth.getInstance().currentUser!!.email) }
            }, {  }) }.show()
        })

        // Create the observer which updates the UI.
        val verifiedObserver = Observer<Boolean> { isVerified ->
            if(isVerified) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        model.isVerified.observe(this, verifiedObserver)

    }
}
