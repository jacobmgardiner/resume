package com.yoloapps.backyardmarket.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.yoloapps.backyardmarket.models.OffersViewModel
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.classes.Type
import com.yoloapps.backyardmarket.utils.UnitUtils
import kotlin.math.floor

class PriceSelectionFragment : Fragment(), AdapterView.OnItemSelectedListener {
    companion object {
        fun newInstance() =
            PriceSelectionFragment()
        const val STATE_COLLAPSED = 0
        const val STATE_EXPANDED = 1

        const val MAX_AMOUNT = 64
        const val MAX_AVAILABLE = 512
    }

    private lateinit var viewModel: OffersViewModel

    private lateinit var number: NumberPicker
    private lateinit var total: NumberPicker
    private lateinit var units: Spinner
    private lateinit var totalUnits: TextView
    private lateinit var cents: EditText
    private lateinit var dollars: EditText
    private lateinit var recText: TextView
    private lateinit var content: LinearLayout
    private lateinit var collapsedText: TextView

    private var unit: Int = -1

    private var selectedType: Type? = null

    private var priceEdited = false
    private var unitTypeChange = false
    private var priceReady = false

    var selectedPrice: Double? = null
        private set
//        set(value) {
//            field = value
//            updatePriceUi()
//            priceEdited = true
//        }

    var selectedUnit: Int? = null
        private set
//        set(value) {
//            field = value
//            if (value != null) {
//                unit = value
//            }
//            priceEdited = true
//        }

    var selectedAmount: Int? = null
        set(value) {
            field = value
            if (value != null) {
                number.value = value
            }
        }

    var selectedTotalAvailable: Int? = null
        set(value) {
            field = value
            if (value != null) {
                total.value = value
            }
        }

    var isRecommendedPrice: Boolean? = null
        private set
        get() {
            return !priceEdited
        }

    var state: Int = 0
        private set

    fun expand(type: Type) {
        selectedType = type
        content.visibility = View.VISIBLE
        collapsedText.visibility = View.GONE
        state =
            STATE_EXPANDED
        unit = -1
        initPrice()
        updatePrice()
    }

    fun collapse() {
        selectedType = null
        selectedPrice = null
        selectedAmount = null
        selectedUnit = null
        selectedTotalAvailable = null
        content.visibility = View.GONE
        collapsedText.visibility = View.VISIBLE
        state =
            STATE_COLLAPSED
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.price_selection_fragment, container, false)

        content = root.findViewById(R.id.content)

        collapsedText = root.findViewById(R.id.collapsed_text)
        recText = root.findViewById(R.id.rec_text)

        number = root.findViewById(R.id.number)
        number.maxValue =
            MAX_AMOUNT
        number.minValue = 1
        number.wrapSelectorWheel = false
        number.value = 1
        number.setOnValueChangedListener { numberPicker: NumberPicker, old: Int, new: Int ->
            if(new > total.value) {
                total.value = new
            }
            selectedAmount = new
            updatePrice()
        }

        total = root.findViewById(R.id.total)
        total.maxValue =
            MAX_AVAILABLE
        total.minValue = 1
        total.wrapSelectorWheel = false
        total.value = 1
        total.setOnValueChangedListener { numberPicker: NumberPicker, i: Int, new: Int ->
            if(new < number.value) {
                number.value = new
                updatePrice()
            }
            selectedTotalAvailable = new
        }

        units = root.findViewById(R.id.unit)
        units.setSelection(0)
        ArrayAdapter.createFromResource(requireContext(),
            R.array.units, android.R.layout.simple_spinner_item)
            .also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                units.adapter = adapter
            }

        units.onItemSelectedListener = this

        totalUnits = root.findViewById(R.id.total_units)

        cents = root.findViewById(R.id.cents)
        cents.onFocusChangeListener = View.OnFocusChangeListener{ view: View, b: Boolean ->
            if(b) {

            } else {
                val old = (view as EditText).text.toString()
                val new = old.padEnd(2, '0')
                (view as EditText).setText(new)
            }
        }
        cents.doOnTextChanged { text, start, count, after ->
            if(cents.isFocused)
                onPriceEdited()
        }
        dollars = root.findViewById(R.id.dollars)
        dollars.doOnTextChanged { charSequence: CharSequence?, i: Int, i1: Int, i2: Int ->
            if(dollars.isFocused)
                onPriceEdited()
        }

        number = root.findViewById(R.id.number)
        number.value = selectedType?.defaultAmount?.toInt() ?: 1
        total.value = number.value
        unitTypeChange = true
        selectedType?.defaultUnit?.let { units.setSelection(it) }

        updatePrice()

        priceReady = true

        collapse()

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OffersViewModel::class.java)
//        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(OffersViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun onPriceEdited() {
        priceEdited = true
        cents.setTextColor(Color.BLACK)
        dollars.setTextColor(Color.BLACK)
        recText.visibility = View.GONE
    }

    private fun initPrice() {
        number.value = selectedType?.defaultAmount?.toInt() ?: 1
        selectedType?.defaultUnit?.let { units.setSelection(it) }
        selectedPrice = selectedType?.recommendedPrice
        updatePriceUi()
    }

    private fun updatePrice() {
        if(!priceEdited && state == STATE_EXPANDED && selectedType != null) {
            val conversion = if(unit != -1) UnitUtils.unitConversionRatio(unit, selectedType!!.defaultUnit!!) else 1.0
            selectedPrice = selectedType!!.recommendedPrice?.times(((number.value) / (selectedType!!.defaultAmount!!)))?.times(conversion)/*?.times(it.defaultAmount)*/
            updatePriceUi()
        }
    }

    private fun updatePriceUi() {
        if (selectedPrice != null) {
            dollars.setText(floor(selectedPrice!!).toInt().toString())
            cents.setText(((selectedPrice!! - floor(selectedPrice!!)) * 100).toInt().toString().padEnd(2, '0'))
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(!isSupportedUnit(position)) {
            parent?.setSelection(unit)
//            Toast.makeText(requireContext(), "Unit incompatible with type!", Toast.LENGTH_LONG).show()
            Snackbar.make(requireView(), "Unit incompatible with type!", Snackbar.LENGTH_LONG).show()
        } else {
            if (unit == -1) {

            } else {
                if (!UnitUtils.canConvert(unit, position))
                    onPriceEdited()
            }
            unit = position
            selectedUnit = position
            totalUnits.text = resources.getStringArray(R.array.units)[unit]
            updatePrice()
        }
    }

    private fun isSupportedUnit(selUnit: Int): Boolean {
        return selectedType?.supportedUnits?.contains(selUnit) ?: true
    }
}