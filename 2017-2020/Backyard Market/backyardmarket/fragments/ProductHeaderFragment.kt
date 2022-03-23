package com.yoloapps.backyardmarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.FirebaseStorageRepository
import com.yoloapps.backyardmarket.data.classes.Product
import com.yoloapps.backyardmarket.utils.PriceFormatUtils
import com.yoloapps.backyardmarket.utils.UnitUtils

class ProductHeaderFragment : Fragment() {
    companion object {
        fun newInstance() =
            ProductHeaderFragment()

        const val DESC_COLLAPSED_MAX_LINES = 4
    }

//    private lateinit var viewModel: SellerHeaderViewModel

    private lateinit var image: ImageView
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var amount: TextView
    private lateinit var unit: TextView
    private lateinit var price: TextView
    private lateinit var imageEdit: FloatingActionButton
    private lateinit var descriptionEdit: FloatingActionButton
//    private lateinit var categoriesFrag: CategoriesFragment

    private var created = false

    var product: Product? = null
        set(value) {
            field = value
            if (product != null) {
                initViews()
            }
        }

    var editable: Boolean = false
        set(value) {
            field = value
            if(product != null) {
                initViews()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        created = true
        val root = inflater.inflate(R.layout.product_header_fragment, container, false)

        image = root.findViewById(R.id.image)
        title = root.findViewById(R.id.title)
        description = root.findViewById(R.id.description)
        description.maxLines = DESC_COLLAPSED_MAX_LINES
        description.setOnClickListener {
            if (description.maxLines == DESC_COLLAPSED_MAX_LINES)
                description.maxLines = Int.MAX_VALUE
            else
                description.maxLines = DESC_COLLAPSED_MAX_LINES
        }
        amount = root.findViewById(R.id.amount)
        unit = root.findViewById(R.id.unit)
        price = root.findViewById(R.id.price)
        ratingBar = root.findViewById(R.id.rating_view)

        imageEdit = root.findViewById(R.id.image_edit)
        imageEdit.visibility = View.GONE

        descriptionEdit = root.findViewById(R.id.desc_edit)
        descriptionEdit.visibility = View.GONE

        return root
    }

    private fun initViews() {
        if(created) {
            if (product?.imageReference != null) {
                FirebaseStorageRepository.loadImage(requireContext(), product?.imageReference!!, image)
            }

            title.text = product!!.title
            description.text = product!!.description ?: "No description"
            //TODO get rating
//            ratingBar.rating = product!!.averageRating ?: 0F
            amount.text = (product!!.amount ?: 1).toString()
            unit.text = UnitUtils.getUnitAsString(requireContext(), product!!.unit ?: 0)
            price.text = PriceFormatUtils.toPriceFormat((product!!.price ?: 0.0))

            if (editable) {
                //TODO make editable

                imageEdit.setOnClickListener {
//                    val intent = Intent(requireContext(), CameraActivity::class.java)
//                    intent.putExtra(CameraActivity.ARG_USER, product as Parcelable)
//                    requireContext().startActivity(intent)
                }
                imageEdit.visibility = View.VISIBLE

                descriptionEdit.setOnClickListener {

                }
                descriptionEdit.visibility = View.VISIBLE
            }
        }
    }
}