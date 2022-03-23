package com.yoloapps.backyardmarket.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import com.yoloapps.backyardmarket.data.classes.Product
import com.yoloapps.location.utils.Geohash
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MapShopViewModel : ViewModel() {
    companion object {
        const val COLLECTION_PRODUCTS: String = "products"

        /**
         * The current geohashes and the products contained therein are currently being fetched from the server.
         */
        const val STATUS_LOADING = 0
        /**
         * The data has been fetched and updated.
         */
        const val STATUS_LOADED = 1
        /**
         * There was some kind of error preventing the data from being fetched or updated.
         */
        const val STATUS_ERROR = -1
    }

    /**
     * Live data hashmap linking a geohash to a list of products within that geohash.
     */
    private val products: MutableLiveData<HashMap<String, List<Product>>> by lazy {
        MutableLiveData<HashMap<String, List<Product>>>()
    }

    /**
     * The model's loading status.
     *
     * Either [STATUS_LOADING], [STATUS_LOADED], or [STATUS_ERROR]
     */
    private val status: MutableLiveData<Int> by lazy { MutableLiveData<Int>(STATUS_LOADING) }

//    private val bitmaps = hashMapOf<Product, Bitmap>()

    /**
     * Contains only the most recently loaded geohashes.
     */
    var newestProducts: HashMap<String, List<Product>> = hashMapOf()
        private set

    /**
     * Used to prevent multiple fetches at once.
     */
    private var loading = false

    /**
     * Method for getting access to the products object for observation.
     */
    fun getProducts(): LiveData<HashMap<String, List<Product>>> {
        if(products.value == null) {
            products.value = hashMapOf<String, List<Product>>()
        }
        return products
    }

    /**
     * Updates the [hashmap of geohashes and products][products] based on the new bounds given.
     */
    fun updateProducts(bottomLeft: GeoPoint, topRight: GeoPoint) {
        //update status to loading
        status.postValue(STATUS_LOADING)

        if(products.value == null)
            products.value = hashMapOf<String, List<Product>>()
        // get all the geohashes in the bounds
        val all = Geohash.geohashesInBounds(
            bottomLeft,
            topRight
        )
        // get rid of duplicates
        val new = arrayListOf<String>()
        for(g in all) {
            if(!products.value!!.containsKey(g)) {
                new.add(g)
            }
        }
        // fetch the products within the new geohashes
        if (new.isNotEmpty()) {
            viewModelScope.launch {
                loadProducts(new)
            }
        }
    }

    /**
     * Fetch the products from the server contained in the given geohashes.
     */
    private suspend fun loadProducts(new: List<String>) {
        if(!loading) {
            loading = true
            val auth = FirebaseAuth.getInstance()
            val user = auth.currentUser
            val productsRef = FirebaseFirestore.getInstance().collection(COLLECTION_PRODUCTS)
            val newProducts = hashMapOf<String, List<Product>>()
            var done = false

            val list = new.withIndex()
                .groupBy { it.index / 10 }
                .map { l -> l.value.map { it.value } }
            for (geohashes in list) {
                done = false
                for(g in geohashes)
                    newProducts[g] = arrayListOf()
                productsRef
                    .whereIn(Product.GEOHASH, geohashes)
                    .orderBy(Product.TIME, Query.Direction.ASCENDING)
                    .get()
                    .addOnSuccessListener { docs ->
//                        val ps = arrayListOf<Product>()
                        for (doc in docs) {
                            val prod = doc.toObject(Product::class.java)
                            when {
                                user == null -> {
                                    (newProducts[prod.g] as ArrayList).add(prod)
                                }
                                prod.sellerName == user.uid -> {
                                    (newProducts[prod.g] as ArrayList).add(prod)
                                }
                                else -> {
                                    (newProducts[prod.g] as ArrayList).add(prod)
                                }
                            }
                        }
//                        newProducts[g] = ps
                    }
                    .addOnCompleteListener { done = true }
            }
            while (!done) {
                delay(1000L)
            }
            updateValue(newProducts)
            loading = false
            status.postValue(STATUS_LOADED)
        }
    }

    /**
     * A convenience function to update the list of products as well as the list of most recently updated products.
     */
    private fun updateValue(new: HashMap<String, List<Product>>) {
        newestProducts = new
        products.value!!.putAll(new)
        products.notifyObserver()
    }

    /**
     * Used to tell the live data to notify the observers of an update by setting the value to itself.
     */
    private fun <T> MutableLiveData<T>.notifyObserver() {
        products.value = products.value
    }
}