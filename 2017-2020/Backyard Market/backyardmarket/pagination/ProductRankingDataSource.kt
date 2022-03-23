package com.yoloapps.backyardmarket.pagination

import android.content.Context
import android.util.Log
import androidx.paging.ItemKeyedDataSource
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.classes.Product
import com.yoloapps.backyardmarket.utils.TimestampUtils

class ProductRankingDataSource(private val locationField: String, private val location: String, private val orderBy: String, private val direction: Query.Direction, private val excludedCategories: List<Int>) : ItemKeyedDataSource<Product, Product>() {
    private val fs by lazy { FirebaseFirestore.getInstance() }
    private val productsRef = fs.collection(FirestoreRepository.Companion.Collection.COLLECTION_PRODUCTS.stringValue)

    override fun getKey(item: Product): Product {
        return item
    }

    private fun getOrderByKey(item: Product?): Any? {
        return when(orderBy) {
            Product.RANKING -> { item?.ranking }
            Product.TIME -> { item?.time }
            Product.PRICE -> { item?.price }
            else -> { item?.ranking }
        }
    }

    private fun getInitOrderByKey(item: Product?): Any? {
        return when(orderBy) {
            Product.RANKING -> {
                if(direction == Query.Direction.ASCENDING)
                    -1
                else
                    Int.MAX_VALUE
            }
            Product.TIME -> {
                if(direction == Query.Direction.ASCENDING)
                    null
                else
                    TimestampUtils.timestamp
            }
            Product.PRICE -> {
                if(direction == Query.Direction.ASCENDING)
                    -1.0
                else
                    Double.MAX_VALUE
            }
            else -> { null }
        }
    }

    override fun loadInitial(params: LoadInitialParams<Product>, callback: LoadInitialCallback<Product>) {
        val list = filterResults(Tasks.await(fetchItems(params.requestedInitialKey, params.requestedLoadSize)).toObjects(Product::class.java))
        list.add(0, Product(title = "header"))
        callback.onResult(list)
    }

    override fun loadAfter(params: LoadParams<Product>, callback: LoadCallback<Product>) {
        callback.onResult(filterResults(Tasks.await(fetchItemsAfter(params.key, params.requestedLoadSize)).toObjects(Product::class.java)))
    }

    private fun filterResults(results: MutableList<Product>): MutableList<Product> {
        val filtered = ArrayList(results)
        for ((i, product) in results.withIndex()) {
            if (excludedCategories.contains(product.category)) {
                filtered.remove(product)
            }
        }
        return filtered
    }

    private fun fetchItems(requestedInitialKey: Product?, requestedLoadSize: Int): Task<QuerySnapshot> {
        return productsRef
            .whereEqualTo(locationField, location)
            .orderBy(orderBy, direction)
            .orderBy(Product.PRODUCT_ID, direction)
            .startAfter(getInitOrderByKey(requestedInitialKey), requestedInitialKey?.productId)
            .limit(requestedLoadSize.toLong())
            .get()
            .addOnSuccessListener { result ->
//                fetched(filterResults(result.toObjects(Product::class.java)))
            }
            .addOnFailureListener {
                Log.e("PPPPPPP", it.message)
            }
    }

    private fun fetchItemsAfter(lastKey: Product, limit: Int): Task<QuerySnapshot> {
        return productsRef
            .whereEqualTo(locationField, location)
            .orderBy(orderBy, direction)
            .orderBy(Product.PRODUCT_ID, direction)
            .startAfter(getOrderByKey(lastKey), lastKey.productId)
            .limit(limit.toLong())
            .get()
            .addOnSuccessListener { result ->
//                fetched(filterResults(result.toObjects(Product::class.java)))
            }
            .addOnFailureListener {
                invalidate()
                Log.e("PPPPPPP", it.message)
            }
    }

    override fun loadBefore(params: LoadParams<Product>, callback: LoadCallback<Product>) {
        Log.d("PPPPP", "LOAD BEFORE: "+params.key.price)
    }
}
