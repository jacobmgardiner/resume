package com.yoloapps.backyardmarket.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import com.yoloapps.backyardmarket.*
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.classes.Offer
import com.yoloapps.backyardmarket.data.classes.Product
import com.yoloapps.backyardmarket.data.classes.UserProfile
import com.yoloapps.backyardmarket.fragments.MessageBarFragment
import com.yoloapps.backyardmarket.fragments.MessagesFragment
import com.yoloapps.backyardmarket.fragments.ConversationHeaderFragment

class ConversationActivity : AppCompatActivity() {
    companion object {
        const val ARG_PRODUCT = "product"
        const val ARG_OFFER = "offer"
        const val ARG_IS_NEW = "new"

        const val ARG_SENDER_UID = "sender"
    }

    private var product: Product? = null
    private var offer: Offer? = null
    private var new: Boolean = false

//    private lateinit var buttons: LinearLayout
    private lateinit var messageBarContainer: FrameLayout

    private lateinit var messagesFrag: MessagesFragment
    private lateinit var headerFragment: ConversationHeaderFragment
    private lateinit var messageBarFragment: MessageBarFragment
    //TODO pay frag

    private var seller = false

    private val repo by lazy { FirestoreRepository.getInstance(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        product = intent.getParcelableExtra(ARG_PRODUCT)
        if (product == null) {
            finish()
            return
        }
        offer = intent.getParcelableExtra(ARG_OFFER)
        new = intent.getBooleanExtra(ARG_IS_NEW, false)

        messageBarContainer = findViewById(R.id.message_bar_conatiner)

        headerFragment = supportFragmentManager.findFragmentById(R.id.header) as ConversationHeaderFragment
        headerFragment.product = product

        Log.d("XXXXXXXXX", "product: " + product)
        Log.d("XXXXXXXXX", "offer: " + offer)
        Log.d("XXXXXXXXX", "is new: " + new)

        if(!new) {
            headerFragment.offer = offer

            messagesFrag = supportFragmentManager.findFragmentById(R.id.messages_fragment) as MessagesFragment
            messagesFrag.offer = offer

            if (product!!.sellerUid != repo.uid) {
                initBuyerView()
            } else {
                initSellerView()
            }
        } else {
            initMakeOffer()
        }
    }

    private fun initMakeOffer() {
        messagesFrag = supportFragmentManager.findFragmentById(R.id.messages_fragment) as MessagesFragment
        messagesFrag.offer = offer

        headerFragment.type = ConversationHeaderFragment.TYPE_INIT_OFFER
        repo.getUserProfileCache(product?.sellerUid!!)
            .addOnSuccessListener {
                headerFragment.user = it.toObject(UserProfile::class.java)
            }
        headerFragment.product = product
        headerFragment.offer = offer
        headerFragment.onSendOffer = {
            Log.d("XXXXXXX", "SENT OFFER!!!!!!!!")
            messageBarFragment = MessageBarFragment.newInstance(offer!!)
            supportFragmentManager.beginTransaction()
                .add(messageBarContainer.id, messageBarFragment)
                .commit()
        }
    }

    private fun initBuyerView() {
        headerFragment.type = ConversationHeaderFragment.TYPE_BUYER
        repo.getUserProfileCache(product?.sellerUid!!)
            .addOnSuccessListener {
                headerFragment.user = it.toObject(UserProfile::class.java)
            }
        headerFragment.product = product
        messageBarFragment = MessageBarFragment.newInstance(offer!!)
        supportFragmentManager.beginTransaction()
            .add(messageBarContainer.id, messageBarFragment)
            .commit()
    }

    private fun initSellerView() {
        messagesFrag.isSeller = true
        headerFragment.type = ConversationHeaderFragment.TYPE_SELLER
        repo.getUserProfileCache(product?.sellerUid!!)
            .addOnSuccessListener {
                headerFragment.user = it.toObject(UserProfile::class.java)
            }
        messageBarFragment = MessageBarFragment.newInstance(offer!!)
        supportFragmentManager.beginTransaction()
            .add(messageBarContainer.id, messageBarFragment)
            .commit()
    }
}
