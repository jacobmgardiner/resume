package com.yoloapps.backyardmarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.yoloapps.backyardmarket.R

class FilterFragment : Fragment() {
    companion object {
        const val ARG_FILTERS = "filters"

        @JvmStatic
        fun newInstance(filters: List<String>) =
            ProductListFragment().apply {
                arguments = Bundle().apply {
                    putStringArray(ARG_FILTERS, filters.toTypedArray())
                }
            }
    }

    private lateinit var chipGroup: ChipGroup

    var filters: List<String>? = null
        set(value) {
            field = value
            initChecked()
            initFilters()
        }

    var checked: MutableLiveData<MutableList<Boolean>?> = MutableLiveData<MutableList<Boolean>?>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.filter_fragment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chipGroup = view.findViewById(R.id.filters)

        initFilters()
    }

    private fun initChecked() {
        checked.postValue(MutableList(filters?.size!!) { true })
    }

    private fun initFilters() {
        if (filters != null && this::chipGroup.isInitialized) {
            chipGroup.removeAllViews()
            for ((i, filter) in filters?.withIndex()!!) {
                val chip: Chip = layoutInflater.inflate(R.layout.chip_view, chipGroup, false) as Chip
                chip.id = i
                chip.isChecked = true
                chip.text = filter
                chip.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
                    val c: Chip = compoundButton as Chip
                    checked.value?.set(c.id, c.isChecked)
                    checked.value = checked.value
                }
                chipGroup.addView(chip)
            }
        }
    }
}