package com.yoloapps.backyardmarket.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.classes.UserProfile
import com.yoloapps.backyardmarket.models.FeaturedViewModel
import com.yoloapps.backyardmarket.views.UserView

class SellerFragment : Fragment() {

    private lateinit var userView: UserView

    private lateinit var viewModel: FeaturedViewModel

    private lateinit var viewContainer: LinearLayout

    private var empty = true

    private var root: View? = null

    var user: UserProfile? = null
        set(value) {
            field = value
            initView()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getParcelable(ARG_USER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.seller_fragment, container, false)
        initView()
        return root
    }

    private fun initView() {
        if(root != null) {
            viewContainer = root!!.findViewById(R.id.container)
            user?.let { viewContainer.addView(UserView(requireContext(), it, false)) }
        }
    }

    companion object {
        const val ARG_USER = "user"

        @JvmStatic
        fun newInstance(user: UserProfile) : SellerFragment =
            SellerFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_USER, user)
                }
            }
    }
}
