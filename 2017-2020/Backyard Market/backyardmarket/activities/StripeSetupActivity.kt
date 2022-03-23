package com.yoloapps.backyardmarket.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import com.google.firebase.auth.FirebaseAuth
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.FirestoreRepository
import kotlinx.android.synthetic.main.activity_stripe_setup.*

class StripeSetupActivity : AppCompatActivity() {
    private val repo by lazy { FirestoreRepository.getInstance(applicationContext) }
    private val state: String = repo.getRandomRedirectState() // generate a unique value for this
    private val clientId: String = "ca_HQmNnIWd7N9GsSge2mqg62NQk1XACK2a" // the client ID found in your platform settings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stripe_setup)

        connect_with_stripe.setOnClickListener {
            // set the redirect_uri to a deep link back into your app to automatically
            // detect when the user has completed the onboarding flow
//            FirestoreRepository.up

            //TODO redirect back to app with deep link
            val redirect = "${resources.getString(R.string.website_home)}/connect/oauth"
            val businessType = "individual"
            val email = FirebaseAuth.getInstance().currentUser?.email
            val firstName = FirebaseAuth.getInstance().currentUser?.displayName?.split(" ")?.get(0)
            val lastName = FirebaseAuth.getInstance().currentUser?.displayName?.split(" ")?.get(1)
            val productDescription = resources.getString(R.string.stripe_product_description)
            val sellerPage = "${resources.getString(R.string.website_home)}/user/${repo.uid}"

            val url = "https://connect.stripe.com/express/oauth/authorize" +
                    "?client_id=$clientId" +
                    "&state=$state" +
                    "&redirect_uri=$redirect" +
                    "&stripe_user[business_type]=$businessType" +
                    "&stripe_user[first_name]=$firstName" +
                    "&stripe_user[last_name]=$lastName" +
                    "&stripe_user[product_description]=$productDescription" +
                    "&stripe_user[url]=$sellerPage" +
                    "&stripe_user[email]=$email"
            val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(this, Uri.parse(url))
        }
    }
}