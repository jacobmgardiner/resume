package com.yoloapps.backyardmarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.models.TypeSelectionViewModel
import com.yoloapps.backyardmarket.data.classes.Type

class TypeSelectionFragment : Fragment() {
    companion object {
        fun newInstance() =
            TypeSelectionFragment()
        const val STATE_COLLAPSED = 0
        const val STATE_EXPANDED = 1
    }

    private lateinit var viewModel: TypeSelectionViewModel

    private lateinit var typesGroup: ChipGroup
    private lateinit var typesSearch: EditText
    private lateinit var question: TextView
    private lateinit var filler: TextView

    private var types: List<Type>? = null

    var onSelection: (type: Type?) -> Unit = {}

    private val repo by lazy { FirestoreRepository.getInstance(requireContext().applicationContext) }

    var selectedCategory: Int? = null
        set(value) {
            field = value
        }

    var selectedType: Type? = null
        private set

    var state: Int = 0
        private set

    fun expand(category: Int) {
        selectedCategory = category
        question.visibility = View.VISIBLE
        typesGroup.visibility = View.VISIBLE
        filler.visibility = View.GONE
        state = 1
        populateTypes()
    }

    fun collapse() {
        selectedType = null
        question.visibility = View.GONE
        typesGroup.visibility = View.GONE
        filler.visibility = View.VISIBLE
        state = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.type_selection_fragment, container, false)

        typesGroup = root.findViewById(R.id.types)
        typesGroup.removeAllViews()
        typesGroup.isSingleSelection = true

        typesSearch = root.findViewById(R.id.types_search)
        typesSearch.visibility = View.GONE

        question = root.findViewById(R.id.question)
        question.visibility = View.GONE

        filler = root.findViewById(R.id.filler)

        return root
    }

    fun populateTypes() {
//        typesSearch.visibility = View.VISIBLE
        typesGroup.removeAllViews()
        typesGroup.isSingleSelection = true
        typesGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == -1) {
                //priceFrag.collapse()
                selectedType = null
                onSelection(selectedType)
            } else {
                //types?.get(checkedId)?.let { priceFrag.expand(it) }
                types?.get(checkedId)?.let { selectedType = it }
                onSelection(selectedType)
            }
        }

        selectedCategory?.let {
            repo.getTypes(it)
                .addOnSuccessListener { types ->
                    this.types = List(types.documents.size) { i ->
                        types.documents[i].toObject(Type::class.java)!!
                    }
                    for ((i, type) in this.types!!.withIndex()) {
                        val chip: Chip = layoutInflater.inflate(R.layout.chip_view, typesGroup, false) as Chip
                        chip.text = type.detailedDisplayName
                        chip.id = i
                        typesGroup.addView(chip)
                    }
                }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(TypeSelectionViewModel::class.java)
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(TypeSelectionViewModel::class.java)
        // TODO: Use the ViewModel
    }
}