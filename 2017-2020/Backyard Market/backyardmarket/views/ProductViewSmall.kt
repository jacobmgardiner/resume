package com.yoloapps.backyardmarket.views

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.yoloapps.backyardmarket.data.classes.Product
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.activities.ProductActivity
import com.yoloapps.backyardmarket.data.FirebaseStorageRepository
import java.text.NumberFormat


class ProductViewSmall : CardView {
    companion object {
//        fun inflate(context: Context, view: ProductView): View? {
//            return inflate(context, R.layout.product_view, view)
//        }
    }

    var product: Product? = null
        set(value) {
            field = value
            update()
        }

    private lateinit var title: TextView
//    private lateinit var description: TextView
    private lateinit var image: ImageView
    private lateinit var price: TextView
    private lateinit var seller: TextView

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, product: Product) : super(context) {
        this.product = product
        inflate(context, R.layout.product_view, this)
        update()
    }

    fun update() {
//        inflate(context, R.layout.product_view, this)

        image = findViewById(R.id.image)
        product?.imageReference?.let { FirebaseStorageRepository.loadImage(context, it, image) }

        title = findViewById(R.id.title)
        title.text = product?.title ?: "null"

//        description = findViewById(R.id.description)

        price = findViewById(R.id.price)
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        try {
            price.text = format.format(product?.price)
        } catch (e: IllegalArgumentException) {
            Log.e("XXXXXXX", e.message)
        }

        val uid = FirebaseAuth.getInstance().currentUser?.uid

//        seller = findViewById(R.id.seller)
//        if(uid != null && product?.sellerUid.toString() == uid) {
//            seller.text = "you"
//        } else {
//            seller.text = product?.sellerName.toString()
//        }

        this.setOnClickListener {
            val intent = Intent(context, ProductActivity::class.java)
            Log.d("XXXXXX", product.toString())
            intent.putExtra(ProductActivity.ARG_PRODUCT, product as Parcelable)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}