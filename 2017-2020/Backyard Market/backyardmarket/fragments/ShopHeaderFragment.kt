package com.yoloapps.backyardmarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.firebase.firestore.Query
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.classes.Product
import kotlinx.android.synthetic.main.shop_header_fragment.*

class ShopHeaderFragment : Fragment(), AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {
    companion object {
        fun newInstance() = ShopHeaderFragment()

        const val SORT_SELLER = 0
        const val SORT_TIME = 1
        const val SORT_PRICE_LOW = 2
        const val SORT_PRICE_HIGH = 3

        const val SORT_SEARCH = 4

        const val SEARCH_RADIUS_MAX = 4
    }

    var searchDistance: MutableLiveData<Int> = MutableLiveData()
        init {
            searchDistance.value = 0
        }
    var orderBy: MutableLiveData<String> = MutableLiveData()
        init {
            orderBy.value = Product.RANKING
        }
    var sortDirection: MutableLiveData<Query.Direction> = MutableLiveData()
        init {
            sortDirection.value = Query.Direction.DESCENDING
        }
    var excludedCategories: MutableLiveData<List<Int>> = MutableLiveData()
        init {
            excludedCategories.value = listOf()
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shop_header_fragment, container, false) as ViewGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //
        // instantiate the spinner
        //
        ArrayAdapter.createFromResource(requireContext(),
            R.array.sort_by, android.R.layout.simple_spinner_item)
            .also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                sortBySpinner.adapter = adapter
            }

        sortBySpinner.onItemSelectedListener = this


        //
        // instantiate the distance bar
        //
        searchRadiusBar.setOnSeekBarChangeListener(this)
        searchRadiusBar.max = SEARCH_RADIUS_MAX
        //distanceBar.min = 1
        searchRadiusBar.progress = searchDistance.value!!
        searchRadiusText.text = "postal code"


        //
        // set up the search filters
        //
        val filters = childFragmentManager.findFragmentById(R.id.filters) as FilterFragment
        filters.filters = resources.getStringArray(R.array.categories).toList()
        filters.checked.observe(
            viewLifecycleOwner,
            Observer { checked ->
                val categories = ArrayList<Int>()
                for ((i, category) in checked!!.withIndex()) {
                    if (!category) categories.add(i)
                }
                excludedCategories.postValue(categories)
            }
        )
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        parent?.setSelection(0)
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(parent!!.getItemAtPosition(position)) {
            resources.getStringArray(R.array.sort_by)[SORT_SELLER] -> {
                sortDirection.value=(Query.Direction.DESCENDING)
                orderBy.postValue(Product.RANKING)
            }
            resources.getStringArray(R.array.sort_by)[SORT_TIME] -> {
                sortDirection.value=(Query.Direction.DESCENDING)
                orderBy.postValue(Product.TIME)
            }
            resources.getStringArray(R.array.sort_by)[SORT_PRICE_LOW] -> {
                sortDirection.value=(Query.Direction.ASCENDING)
                orderBy.postValue(Product.PRICE)
            }
            resources.getStringArray(R.array.sort_by)[SORT_PRICE_HIGH] -> {
                sortDirection.value=(Query.Direction.DESCENDING)
                orderBy.postValue(Product.PRICE)
            }
        }
    }

    private fun updateDistanceText(distance: Int) {
        when (distance) {
            0 -> searchRadiusText.text = ("postal code")
            1 -> searchRadiusText.text = ("city")
            2 -> searchRadiusText.text =("county")
            3 -> searchRadiusText.text =("state")
            4 -> searchRadiusText.text =("country")
        }
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        updateDistanceText(seekBar!!.progress)
        searchDistance.value = seekBar.progress
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        seekBar!!.progress = seekBar.progress / seekBar.max * 4 * seekBar.progress
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        updateDistanceText(seekBar!!.progress)
    }
}