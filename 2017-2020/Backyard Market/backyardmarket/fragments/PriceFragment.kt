package com.yoloapps.backyardmarket.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.yoloapps.backyardmarket.models.OffersViewModel
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.classes.Offer
import com.yoloapps.backyardmarket.data.classes.Product
import com.yoloapps.backyardmarket.utils.PriceFormatUtils
import com.yoloapps.backyardmarket.utils.TimestampUtils
import com.yoloapps.backyardmarket.utils.UnitUtils

class PriceFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var viewModel: OffersViewModel

    private lateinit var amount: NumberPicker
    private lateinit var units: Spinner
    private lateinit var priceView: TextView

    private var unitTypeChange = false

    private var initialized = false

    private var root: View? = null

    private var unit = -1

    private val repo by lazy { FirestoreRepository.getInstance(requireContext().applicationContext) }

    var product: Product? = null
        set(value) {
            field = value
            initViews()
        }

    var price: Double? = null
        private set(value) {
            field = value
            if (value != null)
                priceView.text = PriceFormatUtils.toPriceFormat(value)
        }

    var selectedUnit: Int? = null
        private set

    var selectedAmount: Int? = null
//        set(value) {
//            field = value
//            if (value != null) {
//                amount.value = value
//            }
//        }
        get() {
            return amount.value
        }

    val offer: Offer?
        get() {
            return if(product != null) {
                Offer(
                    productId = product!!.productId,
                    buyerUid = repo.uid,
                    sellerUid = product!!.sellerUid,
                    amount = selectedAmount,
                    unit = selectedUnit,
                    price = price,
                    time = TimestampUtils.timestamp
                )
            } else {
                null
            }
        }

    var state: Int = 0
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            product = it.getParcelable(ARG_PRODUCT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.price_fragment, container, false)
        amount = root!!.findViewById(R.id.amount)
        units = root!!.findViewById(R.id.unit)
        priceView = root!!.findViewById(R.id.price)
        initialized = true
        initViews()
        return root
    }

    fun initViews() {
        if(initialized) {
            amount.maxValue = product?.totalAmount ?: MAX_AMOUNT
            amount.minValue = 1
            amount.wrapSelectorWheel = true
            amount.value = product?.amount ?: product?.type?.defaultAmount?.toInt() ?: 1
            amount.setOnValueChangedListener { numberPicker: NumberPicker, old: Int, new: Int ->
                selectedAmount = new
                updatePrice()
            }

            ArrayAdapter.createFromResource(requireContext(),
                R.array.units, android.R.layout.simple_spinner_item)
                .also { adapter ->
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    // Apply the adapter to the spinner
                    units.adapter = adapter
                }
            units.onItemSelectedListener = this
            val unit = product?.unit ?: product?.type?.defaultUnit ?: 0
            Log.d("XXXXXXXX", "UNIT: "+product?.unit+", "+unit)
            unitTypeChange = true
            units.setSelection(unit)

            price = product?.price

            updatePrice()
        }
    }

    private fun updatePrice() {
        val conversion = if(unit != -1) UnitUtils.unitConversionRatio(unit, product?.unit!!) else 1.0
        price = product?.price?.times(amount.value / product?.amount!!.toDouble())?.times(conversion)/*?.times(it.defaultAmount)*/
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        //TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(!isSupportedUnit(position)) {
            parent?.setSelection(unit)
            Toast.makeText(requireContext(), "Unit incompatible with type!", Toast.LENGTH_LONG).show()
        } else {
            if (unit == -1) {

            } else {
                if (!UnitUtils.canConvert(unit, position)) {
//                    onPriceEdited()
                }
            }
            unit = position
            selectedUnit = position
            updatePrice()
        }
    }

    private fun isSupportedUnit(selUnit: Int): Boolean {
        return product?.type?.supportedUnits?.contains(selUnit) ?: true
    }

    companion object {
        const val ARG_PRODUCT = "product"

        const val MAX_AMOUNT = 64

        @JvmStatic
        fun newInstance(product: Product) : PriceFragment =
            PriceFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PRODUCT, product)
                }
            }
    }
}