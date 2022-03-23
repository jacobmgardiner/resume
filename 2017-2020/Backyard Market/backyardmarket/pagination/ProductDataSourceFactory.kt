package com.yoloapps.backyardmarket.pagination

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.google.firebase.firestore.Query
import com.yoloapps.backyardmarket.data.classes.Product

class ProductDataSourceFactory(private val locationField: String, private val location: String, private val orderBy: String, private val direction: Query.Direction, private val excludedCategories: List<Int>) : DataSource.Factory<Product, Product>() {
    val sourceLiveData = MutableLiveData<ProductRankingDataSource>()
    lateinit var latestSource: ProductRankingDataSource
    override fun create(): DataSource<Product, Product> {
        latestSource =
            ProductRankingDataSource(
                locationField,
                location,
                orderBy,
                direction,
                excludedCategories
            )
        sourceLiveData.postValue(latestSource)
        return latestSource
    }
}
