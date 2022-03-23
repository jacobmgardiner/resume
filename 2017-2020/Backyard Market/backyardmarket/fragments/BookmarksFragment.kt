package com.yoloapps.backyardmarket.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.QuerySnapshot
import com.yoloapps.backyardmarket.data.classes.Product
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.models.BookmarksViewModel

class BookmarksFragment : Fragment() {

    private lateinit var productsFrag: ProductListFragment

    private lateinit var viewModel: BookmarksViewModel

    private lateinit var emptyText: TextView
    private lateinit var stateButton: ImageView
    private lateinit var fragContainer: LinearLayout
    private var empty = false

    var user: String? = null
        set(value) {
            field = value
            initProductFrag()
        }

    var products: List<Product>? = null
        private set
        get() {
            return productsFrag.products
        }

    var state =
        STATE_EXPANDED
        private set

    fun expand() {
        if(this::productsFrag.isInitialized) {
            state =
                STATE_EXPANDED
            fragContainer.visibility = View.VISIBLE
        }
    }

    fun collapse() {
        if(this::productsFrag.isInitialized) {
            state =
                STATE_COLLAPSED
            fragContainer.visibility = View.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bookmarks_fragment, container, false)

        fragContainer = view.findViewById(R.id.fragment_container)

        stateButton = view.findViewById(R.id.state_button)
        stateButton.setOnClickListener {
            when (state) {
                STATE_COLLAPSED -> {
                    expand()
                    stateButton.setBackgroundResource(ARROW_COLLAPSE)
                }
                STATE_EXPANDED -> {
                    collapse()
                    stateButton.setBackgroundResource(ARROW_EXPAND)
                }
            }
        }

        emptyText = view.findViewById(R.id.empty_text)

        productsFrag = childFragmentManager.findFragmentById(R.id.products_fragment) as ProductListFragment
        productsFrag.orientation = ProductListFragment.ORIENTATION_HORIZONTAL

        return view
    }

    private fun initProductFrag() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(BookmarksViewModel::class.java)
        val observer = Observer<HashMap<String, Product>> { bookmarks ->
            productsFrag.products = bookmarks.values.toList()
            Log.d("XXXXXXX", "bookmarked products: $products")
            if(!empty && products!!.isEmpty()) {
                onEmpty(true)
            } else if(empty && products!!.isNotEmpty()) {
                onEmpty(false)
            }
        }
        viewModel.getBookmarks(viewLifecycleOwner).observe(viewLifecycleOwner, observer)
    }

    private fun onEmpty(empty: Boolean) {
        this.empty = empty
        if(empty) {
            emptyText.visibility = View.VISIBLE
        } else {
            emptyText.visibility = View.GONE
        }
    }

    companion object {
        const val ARROW_EXPAND = R.drawable.ic_expand
        const val ARROW_COLLAPSE =
            R.drawable.ic_collapse

        const val ARG_BUYER = "buyer"

        @JvmStatic
        fun newInstance(/*buyer: String*/) =
            ProductListFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_BUYER, buyer)
                }
            }

        const val STATE_EXPANDED = 0
        const val STATE_COLLAPSED = 1
    }
}
