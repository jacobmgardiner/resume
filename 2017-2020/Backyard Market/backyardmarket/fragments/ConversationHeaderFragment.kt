package com.yoloapps.backyardmarket.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.DocumentSnapshot
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.activities.PurchaseActivity
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.classes.Conversation
import com.yoloapps.backyardmarket.data.classes.Offer
import com.yoloapps.backyardmarket.data.classes.Product
import com.yoloapps.backyardmarket.data.classes.UserProfile
import com.yoloapps.backyardmarket.models.ConversationHeaderViewModel
import com.yoloapps.backyardmarket.utils.PriceFormatUtils
import com.yoloapps.backyardmarket.utils.TimestampUtils
import com.yoloapps.backyardmarket.utils.UnitUtils
import com.yoloapps.backyardmarket.views.ProfileImageView

class ConversationHeaderFragment : Fragment(){
    companion object {
        const val TYPE_BUYER = 0
        const val TYPE_SELLER = 1
        const val TYPE_INIT_OFFER = 2
    }

    private lateinit var image: ProfileImageView
    private lateinit var name: TextView
    private lateinit var message1: TextView
    private lateinit var message2: TextView
    private lateinit var price1: TextView
    private lateinit var price2: TextView
    private lateinit var actionButton: Button

    private val viewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(ConversationHeaderViewModel::class.java) }
    private val repo by lazy { FirestoreRepository.getInstance(requireContext().applicationContext) }

    var type: Int = TYPE_BUYER
        set(value) {
            field = value
            initViews()
        }

    var offer: Offer? = null
        set(value) {
            field = value
            initViews()
        }

    var user: UserProfile? = null
        set(value) {
            field = value
            initViews()
        }

    var product: Product? = null
        set(value) {
            field = value
            initViews()
        }

    var onSendOffer = {  }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.conversation_header_fragment, container, false)

        image = view.findViewById(R.id.image)
        name = view.findViewById(R.id.name)
        message1 = view.findViewById(R.id.message1)
        message2 = view.findViewById(R.id.message2)
        price1 = view.findViewById(R.id.price1)
        price2 = view.findViewById(R.id.price2)
        actionButton = view.findViewById(R.id.actionButton)

        initViews()

        return view
    }

    private fun initViews() {
        if (!this::price2.isInitialized || user == null || product == null || offer == null) return
        image.userProfile = user
        name.text = user?.displayName ?: "null"
        when(type) {
            TYPE_BUYER -> {
                message1.text = "asked for "
                message2.text = "You offered to pay "
                price1.text = "" + PriceFormatUtils.toPriceFormat(product?.price!!) + " for " + product?.amount + " " + UnitUtils.getUnitAsString(requireContext(), product?.unit!!)
                price2.text = "" + PriceFormatUtils.toPriceFormat(offer?.price!!) + " for " + offer?.amount + " " + UnitUtils.getUnitAsString(requireContext(), offer?.unit?:0)
                actionButton.text = "Purchase"
                setupAcceptedObserver()
            }
            TYPE_SELLER -> {
                message2.text = "You asked for "
                message1.text = "offered to pay "
                price2.text = "" + PriceFormatUtils.toPriceFormat(product?.price!!) + " for " + product?.amount + " " + UnitUtils.getUnitAsString(requireContext(), product?.unit!!)
                price1.text = "" + PriceFormatUtils.toPriceFormat(offer?.price!!) + " for " + offer?.amount + " " + UnitUtils.getUnitAsString(requireContext(), offer?.unit ?: 0)
                actionButton.visibility = View.VISIBLE
                actionButton.text = "Accept"
                actionButton.setOnClickListener {
                    accept()
                }
            }
            TYPE_INIT_OFFER -> {
                message1.text = "asked for "
                message2.text = "You offered to pay "
                price1.text = "" + PriceFormatUtils.toPriceFormat(product?.price!!) + " for " + product?.amount + " " + UnitUtils.getUnitAsString(requireContext(), product?.unit!!)
                price2.text = "" + PriceFormatUtils.toPriceFormat(offer?.price!!) + " for " + offer?.amount + " " + UnitUtils.getUnitAsString(requireContext(), offer?.unit ?: 0)
                actionButton.visibility = View.VISIBLE
                actionButton.setOnClickListener {
                    sendOffer()
                    actionButton.visibility = View.GONE
                }
            }
        }
    }

    private fun sendOffer() {
        repo.addOffer(offer!!)
        repo.addBuying(product!!)
        repo.startConversation(
            offer!!,
            Conversation(
                listOf(),
                TimestampUtils.timestamp,
                product!!.productId,
                offer,
                listOf(offer?.sellerUid!!, offer?.buyerUid!!)
            )
        )
        onSendOffer()
    }

    private fun purchase() {
        val intent = Intent(requireContext(), PurchaseActivity::class.java)
        intent.putExtra(PurchaseActivity.ARG_OFFER, offer)
        Log.d("XXXXXXXXX", "offer: "+intent.getParcelableExtra<Offer>(PurchaseActivity.ARG_OFFER))
        requireContext().startActivity(intent)
    }

    private fun accept() {
        repo.accept(offer!!)
        actionButton.text = "Undo Accept"
        actionButton.setOnClickListener {
            unaccept()
        }
    }

    private fun unaccept() {
        repo.unaccept(offer!!)
        actionButton.text = "Accept"
        actionButton.setOnClickListener {
            accept()
        }
    }

    private fun setupAcceptedObserver() {
        val observer = Observer<DocumentSnapshot> { snap ->
            if (snap.data == null || snap?.data!!.isEmpty()) return@Observer
            Log.d("XXXXX", "data: ${snap?.data}")
            val acceptedId = snap?.data!![FirestoreRepository.FIELD_ACCEPTED] as String?
            if (acceptedId != null && acceptedId == repo.uid) {
                actionButton.visibility = View.VISIBLE
                actionButton.setOnClickListener {
                    purchase()
                }
            } else {
                actionButton.visibility = View.GONE
            }
        }
        viewModel.getIsAccepted(offer!!).observe(viewLifecycleOwner, observer)
    }
}