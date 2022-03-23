package com.yoloapps.backyardmarket

import android.content.Intent
import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.stripe.android.PaymentConfiguration
import com.yoloapps.backyardmarket.activities.StartActivity
import com.yoloapps.backyardmarket.activities.VerifyActivity
import com.yoloapps.backyardmarket.initializers.LocationInitializer
import com.yoloapps.backyardmarket.utils.LocationUtils

class BackyardApplication : MultiDexApplication() {
    companion object {
        enum class LoginStatus{
            LOGGED_OUT, NOT_VERIFIED, LOGGED_IN
        }
    }

//    private val auth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate() {
        super.onCreate()

        //TODO("initialize global classes, utils, background tasks, etc?")

//        when(loginStatus()) {
//            LoginStatus.LOGGED_IN -> {  }
//            LoginStatus.LOGGED_OUT -> { startActivity(Intent(this, StartActivity::class.java)) }
//            LoginStatus.NOT_VERIFIED -> { startActivity(Intent(this, VerifyActivity::class.java)) }
//            else -> {  }
//        }

        PaymentConfiguration.init(applicationContext, "")
    }

//    private fun loginStatus(): LoginStatus {
//        val user = auth.currentUser
//        return when {
//            user != null -> { LoginStatus.LOGGED_IN }
//            user?.isEmailVerified == false -> { LoginStatus.NOT_VERIFIED }
//            else -> { LoginStatus.LOGGED_OUT }
//        }
//    }
}
