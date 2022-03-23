package com.yoloapps.backyardmarket.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yoloapps.backyardmarket.*
import com.yoloapps.backyardmarket.data.classes.Product
import com.yoloapps.backyardmarket.views.ProductView
import com.yoloapps.backyardmarket.views.ProductViewSmall

/**
 * A fragment with a recycler view that contains a list of products.
 */
class ProductListFragment : Fragment() {
    companion object {
        const val ORIENTATION_HORIZONTAL = LinearLayoutManager.HORIZONTAL
        const val ORIENTATION_VERTICAL = LinearLayoutManager.VERTICAL

        const val ARG_PRODUCTS = "products"

        @JvmStatic
        fun newInstance(products: List<Product>) =
            ProductListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArray(ARG_PRODUCTS, products.toTypedArray())
                }
            }
    }

//    private var paged = false

    private lateinit var recyclerView: RecyclerView

    var headerFragment: Fragment? = null
        set(value) {
            field = value
            Log.d("FFFFFF", "header: $field")
            recyclerView.adapter = value?.let {
                ProductPagedListAdapterHeader(value, recyclerView.layoutManager as LinearLayoutManager)
            }
        }

    var scrolling: Boolean = true
        set(value) {
            field = value
            recyclerView.isNestedScrollingEnabled = value
        }

    var orientation: Int = ORIENTATION_VERTICAL
        set(value) {
            if(value != field) {
                field = value
                onOrientationChange(value)
            }
        }

    private fun onOrientationChange(new: Int) {
        when(new) {
            ORIENTATION_VERTICAL -> {
                (recyclerView.layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.VERTICAL
            }
            ORIENTATION_HORIZONTAL -> {
                (recyclerView.layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
            }
        }
    }

    var pagedList: PagedList<Product>? = null
        set(value) {
            field = value
            if(recyclerView.adapter != null) {
                (recyclerView.adapter as ProductPagedListAdapterHeader).submitList(value)
            } else {
                recyclerView.adapter = value?.let {
                    ProductPagedListAdapterHeader(headerFragment!!, recyclerView.layoutManager as LinearLayoutManager)
                }
            }
        }

    var products: List<Product>? = null
        set(value) {
            field = value

            if(recyclerView.adapter != null) {
                recyclerView.adapter!!.notifyDataSetChanged()
            } else {
                recyclerView.adapter = value?.let {
                    ProductListAdapter()
                }
            }

            orientation = orientation
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            products = it.getParcelableArray(ARG_PRODUCTS)!!.toList() as List<Product>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.product_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
//        recyclerView.adapter = products?.let {
//            if(paged) {
//                ProductPagedListAdapter(recyclerView.layoutManager as LinearLayoutManager)
//            } else {
//                ProductListAdapter(it, recyclerView.layoutManager as LinearLayoutManager)
//            }
//        }
    }

    override fun onResume() {
        super.onResume()
        (recyclerView.layoutManager as LinearLayoutManager).orientation = orientation
    }

    fun attachFragmentToContainer(containerId: Int) {
        Log.d("FFFFFF", "containerId: $containerId")
        Log.d("FFFFFF", "header: $headerFragment")
        if (headerFragment != null) {
            childFragmentManager.beginTransaction()
                .replace(containerId, headerFragment!!)
//                .add(containerId, headerFragment!!)
                .commit()
            Log.d("FFFFFF", "committed")
        }
    }

    private inner class ProductListAdapter() : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

        /**
         * Provide a reference to the type of views that you are using (custom ViewHolder)
         */
        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            lateinit var productView: CardView

            init {
                when(orientation) {
                    LinearLayoutManager.VERTICAL -> {
                        productView = (v as ProductView)
                    }
                    LinearLayoutManager.HORIZONTAL -> {
                        productView = (v as ProductViewSmall)
                    }
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            // Create a new view.
            val productView = when(orientation) {
                LinearLayoutManager.VERTICAL -> {
                    CardView.inflate(parent.context, R.layout.product_view, ProductView(parent.context)) as ProductView
                }
                LinearLayoutManager.HORIZONTAL -> {
                    CardView.inflate(parent.context, R.layout.product_view_small, ProductViewSmall(parent.context)) as ProductViewSmall
                }
                else -> ProductView(parent.context)
            }

            if(productView is ProductView)
            productView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return ViewHolder(productView)
        }

        override fun getItemCount(): Int {
            return products!!.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // Get element from your dataset at this position and replace the contents of the view
            // with that element
            when(orientation) {
                LinearLayoutManager.VERTICAL -> {
                    (holder.productView as ProductView).product = products!![position]
                }
                LinearLayoutManager.HORIZONTAL -> {
                    (holder.productView as ProductViewSmall).product = products!![position]                }
            }
        }

    }

//    private class ProductPagedListAdapter(val layoutManager: LinearLayoutManager) : PagedListAdapter<Product, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<Product>() {
//        // The ID property identifies when items are the same.
//        override fun areItemsTheSame(oldItem: Product, newItem: Product) =
//            oldItem.productId == newItem.productId
//
//        // If you use the "==" operator, make sure that the object implements
//        // .equals(). Alternatively, write custom data comparison logic here.
//        override fun areContentsTheSame(
//            oldItem: Product, newItem: Product) = oldItem == newItem
//    }) {
//
//        class ViewHolder(itemView: View, layoutManager: LinearLayoutManager) : RecyclerView.ViewHolder(itemView) {
//            lateinit var productView: CardView
//
//            init {
//                when (layoutManager.orientation) {
//                    LinearLayoutManager.VERTICAL -> {
//                        productView = (itemView as ProductView)
//                    }
//                    LinearLayoutManager.HORIZONTAL -> {
//                        productView = (itemView as ProductViewSmall)
//                    }
//                }
//            }
//        }
//
//        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//            val productView = when (layoutManager.orientation) {
//                LinearLayoutManager.VERTICAL -> {
//                    CardView.inflate(
//                        parent.context,
//                        R.layout.product_view,
//                        ProductView(parent.context)
//                    ) as ProductView
//                }
//                LinearLayoutManager.HORIZONTAL -> {
//                    CardView.inflate(
//                        parent.context,
//                        R.layout.product_view_small,
//                        ProductViewSmall(parent.context)
//                    ) as ProductViewSmall
//                }
//                else -> ProductView(parent.context)
//            }
//
//            productView.layoutParams = RecyclerView.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT
//            )
//
//            return ViewHolder(productView, layoutManager)
//        }
//
//        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//            val item = getItem(position)
//            when (layoutManager.orientation) {
//                LinearLayoutManager.VERTICAL -> {
//                    ((holder as ViewHolder).productView as ProductView).product = item
//                }
//                LinearLayoutManager.HORIZONTAL -> {
//                    ((holder as ViewHolder).productView as ProductViewSmall).product = item
//                }
//            }
//        }
//    }

    private inner class ProductPagedListAdapterHeader(val headerFragment: Fragment, val layoutManager: LinearLayoutManager) : PagedListAdapter<Product, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<Product>() {
        // The ID property identifies when items are the same.
        override fun areItemsTheSame(oldItem: Product, newItem: Product) =
            oldItem.productId == newItem.productId

        // If you use the "==" operator, make sure that the object implements
        // .equals(). Alternatively, write custom data comparison logic here.
        override fun areContentsTheSame(
            oldItem: Product, newItem: Product) = oldItem == newItem
    }) {
        val TYPE_PRODUCT = 0
        val TYPE_HEADER = 1

        override fun getItemViewType(position: Int): Int {
            return when (getItem(position)?.title) {
                "header" -> TYPE_HEADER
                else -> TYPE_PRODUCT
            }
        }

        inner class ViewHolder(itemView: View, layoutManager: LinearLayoutManager) : RecyclerView.ViewHolder(itemView) {
            lateinit var productView: CardView

            init {
                when (layoutManager.orientation) {
                    LinearLayoutManager.VERTICAL -> {
                        productView = (itemView as ProductView)
                    }
                    LinearLayoutManager.HORIZONTAL -> {
                        productView = (itemView as ProductViewSmall)
                    }
                }
            }
        }

        inner class ViewHolderHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var container: FrameLayout = FrameLayout(itemView.context)

            init {
                container.layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                container.id = View.generateViewId()
                (itemView as ViewGroup).addView(container)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            when (viewType) {
                TYPE_PRODUCT -> {
                    val productView = when (layoutManager.orientation) {
                        LinearLayoutManager.VERTICAL -> {
                            CardView.inflate(
                                parent.context,
                                R.layout.product_view,
                                ProductView(parent.context)
                            ) as ProductView
                        }
                        LinearLayoutManager.HORIZONTAL -> {
                            CardView.inflate(
                                parent.context,
                                R.layout.product_view_small,
                                ProductViewSmall(parent.context)
                            ) as ProductViewSmall
                        }
                        else -> ProductView(parent.context)
                    }

                    productView.layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    return ViewHolder(productView, layoutManager)
                }
                TYPE_HEADER -> {
                    val linlay = LinearLayout(parent.context)
                    linlay.layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    return ViewHolderHeader(linlay)
                }
                else -> {
                    val productView = when (layoutManager.orientation) {
                        LinearLayoutManager.VERTICAL -> {
                            CardView.inflate(
                                parent.context,
                                R.layout.product_view,
                                ProductView(parent.context)
                            ) as ProductView
                        }
                        LinearLayoutManager.HORIZONTAL -> {
                            CardView.inflate(
                                parent.context,
                                R.layout.product_view_small,
                                ProductViewSmall(parent.context)
                            ) as ProductViewSmall
                        }
                        else -> ProductView(parent.context)
                    }

                    productView.layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    return ViewHolder(productView, layoutManager)
                }
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = getItem(position)
            val type = when (position) {
                0 -> 1
                else -> 0
            }
            when (type) {
                TYPE_PRODUCT -> {
                    when (layoutManager.orientation) {
                        LinearLayoutManager.VERTICAL -> {
                            ((holder as ViewHolder).productView as ProductView).product = item
                        }
                        LinearLayoutManager.HORIZONTAL -> {
                            ((holder as ViewHolder).productView as ProductViewSmall).product = item
                        }
                    }
                }
                TYPE_HEADER -> {

                }
            }
        }

        override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
            if(holder is ViewHolderHeader){
                Log.d("FFFFFF", "holder: $holder")
                attachFragmentToContainer(holder.container.id)
            }
            super.onViewAttachedToWindow(holder)
        }
    }
}
