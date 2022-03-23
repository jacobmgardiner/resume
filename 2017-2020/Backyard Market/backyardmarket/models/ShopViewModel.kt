package com.yoloapps.backyardmarket.models

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.pagination.ProductDataSourceFactory
import com.yoloapps.backyardmarket.data.classes.Product
import com.yoloapps.backyardmarket.utils.LocationUtils


class ShopViewModel(application: Application) : AndroidViewModel(application) {
    val app: Application = application
    private val repo by lazy { FirestoreRepository.getInstance(app.applicationContext) }
    private val locutils by lazy { LocationUtils.getInstance(app.applicationContext) }
    companion object {
        const val DEFAULT_DISTANCE: Int = 0
        val DEFAULT_SORT = Product.RANKING

        const val PAGE_SIZE = 5
        const val MAX_LOAD_SIZE = 200

        val fields = listOf(Product.POSTAL, Product.CITY, Product.COUNTY, Product.STATE, Product.COUNTRY)
    }

    private var context: Context? = null

    var loading = false

    var excludedCategories: List<Int> = listOf()
        set(value) {
            field = value
            newFactory()
        }

    var distance: Int = DEFAULT_DISTANCE
        set(value) {
            field = value
            newFactory()
        }

    var orderBy: String = DEFAULT_SORT
        set(value) {
            field = value
            newFactory()
        }

    var direction = Query.Direction.DESCENDING
        set(value) {
            field = value
            newFactory()
        }

    private var location: String? = null

    private fun newFactory() {
        //TODO("fix nonsense")
        locutils.getLocation { loc ->
            location = context?.let { it1 -> locutils.getLocationAsString(it1, loc) }?.get(distance)

            factory =
                ProductDataSourceFactory(
                    fields[distance],
                    location!!,
                    orderBy,
                    direction,
                    excludedCategories
                )
        }
    }

//    private fun initProducts() {
//        val loc = locutils.getLocation { newFactory() }
//        location = context?.let {
//                it1 -> locutils.getLocationAsString(it1, loc)
//        }?.get(distance)
//
//        factory =
//            ProductDataSourceFactory(
//                fields[distance],
//                location!!,
//                orderBy,
//                direction,
//                excludedCategories
//            )
//    }

    var factory: ProductDataSourceFactory? = null
        private set(value) {
            field = value
            if(value != null) {
                products = value.toLiveData(config = config/*, boundaryCallback = boundaryCallback*/)
            }
        }

    private var config = PagedList.Config.Builder()
        .setInitialLoadSizeHint(PAGE_SIZE)
        .setPageSize(PAGE_SIZE)
        .setMaxSize(MAX_LOAD_SIZE)
        .setEnablePlaceholders(false)
        .build()

    private var products: LiveData<PagedList<Product>>? = null

    fun getProducts(context: Context, location: GeoPoint): LiveData<PagedList<Product>> {
        this.context = context
        this.location = locutils.getLocationAsString(context, location)[distance]
        newFactory()
        return products!!
    }
}
