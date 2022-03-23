package com.yoloapps.backyardmarket.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.models.SignupViewModel


class SignupActivity : AppCompatActivity() {
    val TAG = "BM.Signup"
    lateinit var model: SignupViewModel
    lateinit var first: TextView
    lateinit var last: TextView
    lateinit var email: TextView
    lateinit var password: TextView
    lateinit var password2: TextView
    lateinit var coordinator: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        model = ViewModelProviders.of(this).get(SignupViewModel::class.java)

        first = findViewById(R.id.first)
        last = findViewById(R.id.last)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        password2 = findViewById(R.id.password2)
        coordinator = findViewById(R.id.coordinator)
    }

    fun signup(view: View) {
        val e = email.text.toString()
        val p = password.text.toString()
        val p2 = password2.text.toString()
        val f = first.text.toString()
        val l = last.text.toString()
        if(f.length > 1 && l.length > 1) {
            if (e.length > 3 && e.contains("@")) {
                if(p == p2) {

                    model.signup(this, f, l, e, p, {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")

                        val intent = Intent(this, VerifyActivity::class.java)
                        startActivity(intent)
                    }, {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", it)
//                        Toast.makeText(
//                            this, "Authentication failed.",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        Snackbar.make(coordinator, "Authentication Failed", Snackbar.LENGTH_LONG).show()
                    })
                } else {
//                    Toast.makeText(
//                        this, "Passwords must match.",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    Snackbar.make(coordinator, "Passwords do not match", Snackbar.LENGTH_LONG).show()
                }
            } else {
//                Toast.makeText(
//                    this, "Invalid email.",
//                    Toast.LENGTH_SHORT
//                ).show()
                Snackbar.make(coordinator, "Invalid email", Snackbar.LENGTH_LONG).show()
            }
        } else {
//            Toast.makeText(
//                this, "Invalid name.",
//                Toast.LENGTH_SHORT
//            ).show()
            Snackbar.make(coordinator, "Invalid name", Snackbar.LENGTH_LONG).show()
        }
    }
}
