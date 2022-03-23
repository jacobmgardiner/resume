package com.yoloapps.backyardmarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.yoloapps.backyardmarket.R

class ProductTextInputFragment : Fragment() {
    companion object {
        fun newInstance() =
            ProductTextInputFragment()
    }

    private lateinit var titleEditText: EditText
    private lateinit var descriptionEditText: EditText

    var title: String? = null
        private set
        get() {
            return titleEditText.text.toString()
        }

    var description: String? = null
        private set
        get() {
            return descriptionEditText.text.toString()
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.product_text_input_fragment, container, false)

        titleEditText = root.findViewById(R.id.title)
        descriptionEditText = root.findViewById(R.id.description)

        return root
    }
}