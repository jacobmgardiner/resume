package com.yoloapps.backyardmarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.yoloapps.backyardmarket.models.OffersViewModel
import com.yoloapps.backyardmarket.data.classes.Product
import com.yoloapps.backyardmarket.R

class LoadingFragment : Fragment() {
    companion object {
        fun newInstance() =
            LoadingFragment()
//        const val ARG_PRODUCT = "product"
    }

    private lateinit var viewModel: OffersViewModel
    private lateinit var product: Product
    private lateinit var list: LinearLayout

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