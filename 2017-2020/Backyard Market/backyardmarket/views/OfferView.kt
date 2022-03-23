package com.yoloapps.backyardmarket.views

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.activities.ConversationActivity
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.classes.Offer
import com.yoloapps.backyardmarket.data.classes.Product
import com.yoloapps.backyardmarket.data.classes.UserProfile
import com.yoloapps.backyardmarket.utils.TimestampUtils
import com.yoloapps.backyardmarket.utils.UnitUtils
import java.text.NumberFormat


class OfferView (context: Context, val offer: Offer) : CardView(context) {
    //TODO("make inflatable")
//    constructor(context: Context, attributeSet: AttributeSet) : this() {
//
//    }

    private lateinit var buyer: TextView
    private lateinit var price: TextView
    private lateinit var time: TextView
    private lateinit var image: ProfileImageView

    private val repo by lazy { FirestoreRepository.getInstance(context.applicationContext) }

    init {
        inflate(context, R.layout.offer_view, this)

        buyer = findViewById(R.id.buyer)
        repo.getUserProfileCache(offer.buyerUid!!)
            .addOnSuccessListener {
                val user = it.toObject(UserProfile::class.java)
                buyer.text = user?.displayName

                image = findViewById(R.id.image)
                image.userProfile = user
            }

        price = findViewById(R.id.price)
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        price.text = "" + offer.amount + " " + offer.unit?.let {
            UnitUtils.getUnitAsString(context, it)
        } + " for " + format.format(offer.price)

        time = findViewById(R.id.time)
        time.text = offer.time?.let { TimestampUtils.timestampToDate(it) }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        this.setOnClickListener {
            repo.getProductCache(offer.productId!!)
                .addOnSuccessListener {
                    val product = it.toObject(Product::class.java)
                    val intent = Intent(context, ConversationActivity::class.java)
                    intent.putExtra(ConversationActivity.ARG_OFFER, offer)
                    intent.putExtra(ConversationActivity.ARG_PRODUCT, product)
                    context.startActivity(intent)
                }
        }
    }
}