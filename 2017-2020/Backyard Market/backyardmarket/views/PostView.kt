package com.yoloapps.backyardmarket.views

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.yoloapps.backyardmarket.data.classes.Product
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.activities.ProductActivity


class PostView (context: Context) : CardView(context) {
    companion object {
        const val EXTRA_PRODUCT = "product"
    }
    private lateinit var product: Product

    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var image: ImageView
    private lateinit var price: TextView

    constructor(context: Context, product: Product) : this(context) {
        update(product)
    }

    fun update(product: Product) {
        this.product = product

        inflate(context, R.layout.product_view, this)

        image = findViewById(R.id.image)
        if(product.imageReference != null) {
            val ref = FirebaseStorage.getInstance().getReference(product.imageReference!!)
            Log.d("XXXXXXXXXXXXXXXx", "referece path: " + ref.path)
            Glide.with(this /* context */)
                .load(ref)
                .into(image)
        }

        title = findViewById(R.id.title)
        title.text = product.title

        description = findViewById(R.id.description)
        description.text = product.description

        price = findViewById(R.id.price)
        price.text = product.price.toString()

//        MyUtils.getLocation {
//            var distance = floatArrayOf(0f, 0f, 0f)
//            Location.distanceBetween(product.l!!.latitude, product.l!!.longitude, it.latitude, it.longitude, distance)
//            Log.d("XXXXXXXXXXXXXXx", "distance of product from user: " + distance[0]/1000)
//        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        this.setOnClickListener {
            val intent = Intent(context, ProductActivity::class.java)
            intent.putExtra(ProductActivity.ARG_PRODUCT, product as Parcelable)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}