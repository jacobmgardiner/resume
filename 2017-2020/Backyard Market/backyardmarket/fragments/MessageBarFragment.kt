package com.yoloapps.backyardmarket.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.classes.Message
import com.yoloapps.backyardmarket.data.classes.Offer
import com.yoloapps.backyardmarket.models.MessageBarViewModel
import com.yoloapps.backyardmarket.utils.KeyboardUtils
import com.yoloapps.backyardmarket.utils.TimestampUtils

class MessageBarFragment : Fragment() {
    companion object {

        const val ARG_OFFER = "conversationId"

        @JvmStatic
        fun newInstance(offer: Offer) =
            MessageBarFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_OFFER, offer)
                }
            }
    }

    private lateinit var viewModel: MessageBarViewModel

    private lateinit var message: EditText
    private lateinit var send: Chip

    /**
     * The id for the conversation. The id is the product id followed by the offer number (the index of the offer in the product's offers array).
     */
    var offer: Offer? = null

    var onSent = {  }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            offer = it.getParcelable(ARG_OFFER)
            Log.d("XXXXXXXXXX", "onCreate:conversationId: "+offer)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.message_bar_fragment, container, false)

        message = view.findViewById(R.id.message)
        send = view.findViewById(R.id.send_chip)
        send.setOnClickListener {
            send()
        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(MessageBarViewModel::class.java)
    }

    fun send() {
        if(offer == null || message.text.isEmpty()) {
            return
        }
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val mes = if(uid != offer!!.sellerUid) {
            Message(
                senderUid = uid,
                receiverUid = offer!!.sellerUid,
                seller = false,
                content = message.text.toString(),
                timeSent = TimestampUtils.timestamp,
                productId = offer!!.productId,
                uids = listOf(uid, offer!!.sellerUid!!)
            )
        } else {
            Message(
                senderUid = uid,
                receiverUid = offer!!.buyerUid,
                seller = true,
                content = message.text.toString(),
                timeSent = TimestampUtils.timestamp,
                productId = offer!!.productId,
                uids = listOf(uid, offer!!.buyerUid!!)
            )
        }
        message.clearFocus()
        message.setText("")
        KeyboardUtils.hideKeyboardFrom(requireContext(), message)
        viewModel.sendMessage(offer!!, mes)
        onSent()
    }
}