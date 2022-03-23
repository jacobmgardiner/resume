package com.yoloapps.backyardmarket.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.classes.Suggestion
import com.yoloapps.backyardmarket.models.TipsViewModel
import com.yoloapps.backyardmarket.views.SuggestionView

class TipsFragment : Fragment() {

    private lateinit var suggestionView: SuggestionView

    private lateinit var viewModel: TipsViewModel

    private lateinit var stateButton: ImageView
    private lateinit var contentContainer: LinearLayout
    private var empty = false

    var user: String? = null
        set(value) {
            field = value
            initSuggestionView()
        }

    var state =
        STATE_EXPANDED
        private set

    fun expand() {
        state =
            STATE_EXPANDED
        contentContainer.visibility = View.VISIBLE
    }

    fun collapse() {
        state =
            STATE_COLLAPSED
        contentContainer.visibility = View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.tips_fragment, container, false)

        contentContainer = view.findViewById(R.id.content_container)

        stateButton = view.findViewById(R.id.state_button)
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

        return view
    }

    private fun initSuggestionView() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(TipsViewModel::class.java)
        val observer = Observer<QuerySnapshot> { suggestion ->
            Log.d("XXXXX", "adding view !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11")
            contentContainer.removeAllViews()
            contentContainer.addView(
                SuggestionView(
                    requireContext(),
                    suggestion.documents[0].toObject(Suggestion::class.java)!!
                )
            )
        }
        viewModel.getSuggestion().observe(viewLifecycleOwner, observer)
    }

    companion object {
        const val ARROW_EXPAND = R.drawable.ic_expand
        const val ARROW_COLLAPSE =
            R.drawable.ic_collapse

        const val ARG_BUYER = "buyer"

        @JvmStatic
        fun newInstance(/*buyer: String*/) =
            ProductListFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_BUYER, buyer)
                }
            }

        const val STATE_EXPANDED = 0
        const val STATE_COLLAPSED = 1
    }
}
