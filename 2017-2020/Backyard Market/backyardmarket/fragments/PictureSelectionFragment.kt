package com.yoloapps.backyardmarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.models.OffersViewModel

class PictureSelectionFragment : Fragment() {
    companion object {
        fun newInstance() =
            PictureSelectionFragment()
    }

    private lateinit var viewModel: OffersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.offers_fragment, container, false)

//        val bundle = this.arguments
//        if (bundle != null) {
//            product = bundle.getParcelable(ARG_PRODUCT)!!
//        }



        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OffersViewModel::class.java)
//        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(OffersViewModel::class.java)
        // TODO: Use the ViewModel
    }
}