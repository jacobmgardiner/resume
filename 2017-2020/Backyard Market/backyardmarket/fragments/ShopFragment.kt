package com.yoloapps.backyardmarket.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.models.ShopViewModel
import com.yoloapps.backyardmarket.utils.LocationUtils


class ShopFragment : Fragment() {

    companion object {
        fun newInstance() = ShopFragment()
    }

    private val viewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(ShopViewModel::class.java) }
    private val locutils by lazy { LocationUtils.getInstance(requireContext().applicationContext) }

    private lateinit var header: ShopHeaderFragment
    private lateinit var list: ProductListFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shop_fragment, container, false) as ViewGroup
    }

    override fun setMenuVisibility(visible: Boolean) {
        super.setMenuVisibility(visible)
        if (visible) {
            if (activity != null) {
                (activity as AppCompatActivity).supportActionBar?.title = "Nearby Products"
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        header = childFragmentManager.findFragmentById(R.id.header) as ShopHeaderFragment
        header = ShopHeaderFragment.newInstance()

        list = childFragmentManager.findFragmentById(R.id.list) as ProductListFragment
        list.headerFragment = header

        setupObservers()

        createObserver()
    }

    private fun createObserver() {
        //TODO("fix location nonsense")
        locutils.getLocation {
            viewModel.getProducts(requireContext(), it)
                .observe(viewLifecycleOwner, Observer { products ->
                    for (product in products)
                        Log.d("PPPPPPPPP", "OBSERVED: " + product?.productId)
                    list.pagedList = products
                })
        }
    }

    private fun setupObservers() {
        header.orderBy.observe(viewLifecycleOwner, Observer {
            val dir = header.sortDirection.value!!
            viewModel.direction = dir
            viewModel.orderBy = it
            createObserver()
        })
        header.searchDistance.observe(viewLifecycleOwner, Observer {
            Log.d("XXXXXXX", "distance: $it")
            viewModel.distance = it
            createObserver()
        })
        header.excludedCategories.observe(viewLifecycleOwner, Observer {
            viewModel.excludedCategories = it
            createObserver()
        })
    }
}
