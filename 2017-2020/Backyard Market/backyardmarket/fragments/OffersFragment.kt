package com.yoloapps.backyardmarket.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.yoloapps.backyardmarket.views.OfferView
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.classes.Offer


class OffersFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(offers: List<Offer>) : OffersFragment =
            OffersFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArray(ARG_OFFERS, offers.toTypedArray())
                }
            }
        const val ARG_OFFERS = "offers"
        @JvmStatic
        fun newInstance(offer: Offer) : OffersFragment =
            OffersFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_OFFER, offer)
                }
            }
        const val ARG_OFFER = "offer"


        const val STATE_EXPANDED = 0
        const val STATE_COLLAPSED = 1

        const val ARROW_EXPAND = R.drawable.ic_expand
        const val ARROW_COLLAPSE = R.drawable.ic_collapse
    }

    private var offers: List<Offer>? = null
    private var offer: Offer? = null
    private lateinit var list: LinearLayout
    private lateinit var title: TextView
    private lateinit var stateButton: ImageView
    private lateinit var emptyText: TextView

    private var empty = true

    var state = STATE_EXPANDED
        private set

    fun expand() {
        if(this::list.isInitialized) {
            state = STATE_EXPANDED
            list.visibility = View.VISIBLE
        }
    }

    fun collapse() {
        if(this::list.isInitialized) {
            state =
                STATE_COLLAPSED
            list.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.offers_fragment, container, false)

        val bundle = this.arguments
        if (bundle != null) {
            offers = (bundle.getParcelableArray(ARG_OFFERS) as Array<Offer>?)?.toList()
            offer = bundle.getParcelable(ARG_OFFER) as Offer?
        }

        list = root.findViewById(R.id.list)
        title = root.findViewById(R.id.title)
        stateButton = root.findViewById(R.id.state_button)
        stateButton.setOnClickListener {
            when (state) {
                STATE_COLLAPSED -> {
                    expand()
                    stateButton.setBackgroundResource(ARROW_COLLAPSE)
                }
                STATE_EXPANDED -> {
                    collapse()
                    stateButton.setBackgroundResource(ARROW_EXPAND)
                }
            }
        }
        emptyText = root.findViewById(R.id.empty_text)

        if(offer != null) {
            title.text = "Your Offer"
            onEmpty(false)
            list.addView(
                OfferView(requireContext(), offer!!)
            )
        }
        else if (offers != null) {
            onEmpty(offers!!.isEmpty())
            for ((i, offer) in offers!!.withIndex()) {
                list.addView(
                    OfferView(requireContext(), offer)
                )
            }
        } else {
            onEmpty(true)
        }

        return root
    }

    private fun onEmpty(empty: Boolean) {
        this.empty = empty
        if(empty) {
            emptyText.visibility = View.VISIBLE
        } else {
            emptyText.visibility = View.GONE
        }
    }
}
