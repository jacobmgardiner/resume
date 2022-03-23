package com.yoloapps.backyardmarket.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.stripe.android.ApiResultCallback
import com.stripe.android.PaymentConfiguration
import com.stripe.android.PaymentIntentResult
import com.stripe.android.Stripe
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.StripeIntent
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.classes.Offer
import java.lang.ref.WeakReference
import kotlinx.android.synthetic.main.activity_purchase.*


class PurchaseActivity : AppCompatActivity() {
    companion object {
        const val ARG_OFFER = "offer"
    }
    private var offer: Offer? = null

    private lateinit var paymentIntentClientSecret: String
    private lateinit var stripe: Stripe

    private val repo by lazy { FirestoreRepository.getInstance(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase)

        offer = intent.getParcelableExtra(ARG_OFFER)
        Log.d("XXXX", "offer: $offer")

        startCheckout()
    }

//    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
//        return super.onCreateView(name, context, attrs)
//        startCheckout()
//    }

    private fun startCheckout() {
        // Request a PaymentIntent from your server and store its client secret in paymentIntentClientSecret
        repo.requestPaymentIntent(offer?.productId!!) {
            paymentIntentClientSecret = it
        }

        // Hook up the pay button to the card widget and stripe instance)
        payButton.setOnClickListener {
            val params = cardInputWidget.paymentMethodCreateParams
            if (params != null) {
                val confirmParams = ConfirmPaymentIntentParams
                    .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret, null, false,
                        mapOf("setup_future_usage" to "off_session"))
                stripe = Stripe(applicationContext, PaymentConfiguration.getInstance(applicationContext).publishableKey)
                stripe.confirmPayment(this, confirmParams)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val weakActivity = WeakReference<Activity>(this)

        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, object : ApiResultCallback<PaymentIntentResult> {
            override fun onSuccess(result: PaymentIntentResult) {
                val paymentIntent = result.intent
                val status = paymentIntent.status
                if (status == StripeIntent.Status.Succeeded) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    displayAlert(weakActivity.get(), "Payment succeeded", gson.toJson(paymentIntent)/*, restartDemo = true*/)
                } else {
                    displayAlert(weakActivity.get(), "Payment failed", paymentIntent.lastPaymentError?.message ?: "")
                }
            }

            override fun onError(e: Exception) {
                displayAlert(weakActivity.get(), "Payment failed", e.toString())
            }
        })
    }

    private fun displayAlert(get: Activity?, s: String, toJson: String) {
        Snackbar.make(coordinator, s+toJson, Snackbar.LENGTH_LONG)
    }
}
