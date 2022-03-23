package com.yoloapps.backyardmarket.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.yoloapps.backyardmarket.models.LoginViewModel
import com.yoloapps.backyardmarket.R

class LoginActivity : AppCompatActivity() {
    val TAG = "BM.Login"
    lateinit var model: LoginViewModel
    lateinit var email: TextView
    lateinit var password: TextView
    lateinit var coordinator: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        model = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        coordinator = findViewById(R.id.coordinator)
    }

    fun forgot(view: View) {
        //TODO: reset password activity
    }

    fun login(view: View) {
        val e = email.text.toString()
        val p = password.text.toString()
        if (e.length > 3 && e.contains("@")) {
            if (p.length > 5) {
                model.login(this, e, p, {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }, {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", it)
//                    Toast.makeText(
//                        this, "Authentication failed.",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    Snackbar.make(coordinator, "Authetication failed", Snackbar.LENGTH_LONG)
                        .setAction("Reset Password") { forgot(coordinator) }.show()
                })
            } else {
//                Toast.makeText(
//                    this, "Invalid password.",
//                    Toast.LENGTH_SHORT
//                ).show()
                Snackbar.make(coordinator, "Invalid Password", Snackbar.LENGTH_LONG).show()
            }
        } else {
//            Toast.makeText(
//                this, "Invalid email.",
//                Toast.LENGTH_SHORT
//            ).show()
            Snackbar.make(coordinator, "Invalid email", Snackbar.LENGTH_LONG).show()
        }
    }
}
