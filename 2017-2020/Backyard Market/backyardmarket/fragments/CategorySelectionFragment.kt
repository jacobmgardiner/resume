package com.yoloapps.backyardmarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.yoloapps.backyardmarket.R

class CategorySelectionFragment : Fragment() {
    companion object {
        fun newInstance() =
            CategorySelectionFragment()
    }

    private lateinit var category: RadioGroup

    var onSelection: (int: Int?) -> Unit = {}

    var selectedCategory: Int? = null
        set(value) {
            field = value
            if (value != null) {
                category.check(value)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.category_selection_fragment, container, false)

        category = root.findViewById(R.id.category)

        category.removeAllViews()
        val categories = resources.getStringArray(R.array.categories)
        for((i, c) in categories.withIndex()) {
            val rb = RadioButton(requireContext())
            rb.text = c
            rb.id = i
            category.addView(rb)
        }
        category.setOnCheckedChangeListener { radioGroup: RadioGroup, category: Int ->
            selectedCategory = category
            onSelection(category)
        }

        return root
    }
}