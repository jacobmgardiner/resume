package com.yoloapps.backyardmarket.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.firestore.GeoPoint
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.activities.MainActivity
import com.yoloapps.backyardmarket.activities.UserActivity
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.classes.Product
import com.yoloapps.backyardmarket.models.MapShopViewModel
import com.yoloapps.backyardmarket.utils.IconUtils
import com.yoloapps.backyardmarket.utils.LocationUtils
import com.yoloapps.location.utils.Geohash

/**
 * TODO("doc class")
 * TODO("fix bugs")
 * TODO("implement [loading state][MapShopViewModel.status] anim")
 */
class MapShopFragment : Fragment(), GoogleMap.OnMarkerDragListener {
    companion object {
        private const val PARAM_START_GEOHASH = "geohash"
        private const val PARAM_START_DISTANCE = "distance"
        /**
         * Factory method.
         * @param [geohash][Geohash] The starting location of the map.
         * @param distance The starting zoom of the map (i.e. number of [geohashes][Geohash] to show from the center).
         * @return A new instance of fragment MapShopFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(geohash: String, distance: Int) =
            MapShopFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_START_GEOHASH, geohash)
                    putInt(PARAM_START_DISTANCE, distance)
                }
            }

        @JvmStatic
        fun newInstance() = MapShopFragment()
    }

    /**
     * The starting location of the map.
     * Null if none was set.
     */
    private var startGeohash: String? = null
    /**
     * The starting zoom of the map (i.e. number of [geohashes][Geohash] to show from the center).
     * Null if none was set.
     */
    private var startDistance: Int? = null

//    private val PERMISSION_REQUEST_LOCATION = 1

    /**
     * If true, displays extra information.
     * Displays a window above a marker when its clicked containing
     */
    private var debug = false

    /**
     * The Google Map object passed into the handler of [SupportMapFragment.getMapAsync].
     * Null before [SupportMapFragment.getMapAsync] is called.
     */
    private lateinit var map: GoogleMap

//    private var polygons: HashMap<String, Polygon> = hashMapOf()
    /**
     * A list of all the markers added to the map.
     */
    private var markers = arrayListOf<Marker>()

//    private var viewModel: MapShopViewModel = MapShopViewModel by activityViewModels()

    /**
     * The view model.
     */
    private val viewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(MapShopViewModel::class.java) }

    /**
     * A map linking a [geohash][Geohash] to a list of the [products][Product] within that geohash.
     */
    private var productsMap = hashMapOf<String, List<Product>>()
    /**
     * A list of the selected products (i.e. the products in the currently selected geohash).
     * Passed to the [ProductListFragment] in order to populate it.
     */
    private var selectedProducts = arrayListOf<Parcelable>()

    private val primaryColor by lazy { ContextCompat.getColor(requireContext(), R.color.colorPrimary) }
    private val primaryColorDark by lazy { ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark) }
    private val clearColor by lazy { ContextCompat.getColor(requireContext(), R.color.colorClear) }

    private val productsColor by lazy { Color.argb(100, Color.red(primaryColor), Color.green(primaryColor), Color.blue(primaryColor)) }
    private val strokeColor by lazy { Color.argb(200, Color.red(primaryColorDark), Color.green(primaryColorDark), Color.blue(primaryColorDark)) }
    private val emptyColor by lazy { Color.argb(50, Color.red(Color.LTGRAY), Color.green(Color.LTGRAY), Color.blue(Color.LTGRAY)) }

//    private var grid = arrayListOf<Polygon>()

    /**
     * TODO("???")
     */
    private var markerTypes = hashMapOf<String, Int>()

    /**
     * The root view of the inflated layout.
     * Null before inflation.
     */
    private var rootView: View? = null
        set(value) {
            field = value
//            bottomSheet = value?.findViewById<LinearLayout>(R.id.products_sheet)
//            sheetBehavior = bottomSheet?.let { BottomSheetBehavior.from(it) } as BottomSheetBehavior<LinearLayout>
        }

    private val bottomSheet by lazy { rootView?.findViewById<LinearLayout>(R.id.products_sheet) }
    private val sheetBehavior: BottomSheetBehavior<LinearLayout> by lazy { bottomSheet?.let { BottomSheetBehavior.from<LinearLayout>(it) } as BottomSheetBehavior<LinearLayout> }

    var Marker.type: Int?
        get() { return markerTypes[this.id] }
        set(value) { markerTypes[this.id] = value!! }

    private val TYPE_CENTER: Int
        get() = -1

    private val TYPE_SELLER: Int
        get() = -2

    private var selectedMarker: Marker? = null

    private val locutil by lazy { LocationUtils.getInstance(requireContext().applicationContext) }
    private val repo by lazy { FirestoreRepository.getInstance(requireContext().applicationContext) }

    private val productsObserver =
        Observer<HashMap<String, List<Product>>> { productsMap ->
            onProductsMapUpdate(productsMap)
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            startGeohash = it.getString(PARAM_START_GEOHASH)
            startDistance = it.getInt(PARAM_START_DISTANCE)
        }
    }


    //TODO("remove boilerplate??")
    override fun setMenuVisibility(visible: Boolean) {
        super.setMenuVisibility(visible)
        if (visible) {
            if (activity != null) {
                (activity as AppCompatActivity).supportActionBar?.title = "Products Map"
            }
            requestPermission()
        }
    }

    /**
     * Requests the necessary permissions (location) from the user.
     */
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            MainActivity.Companion.PermissionRequest.LOCATION.ordinal
        )
    }

    /**
     * Called when location permission has been granted by the user.
     * Called by MainActivity.onRequestPermissionsResult and when the view is created if permissions have already been granted.
     */
    fun onPermissionGranted() {
        setupMap()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.map_shop_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rootView = view
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//        sheetBehavior.addBottomSheetCallback(SheetCallback(mMap))

        if (locutil.permissionGranted())
            onPermissionGranted()
        else
            onPermissionDenied()

        //TODO("permission stuff")
        //TODO("call setupMap()")
    }

    /**
     * Called when the location permission has not been granted by the time the view has been created.
     */
    fun onPermissionDenied() {
        //TODO("show message with button to grant permissions")
    }

    /**
     * Sets up the Google Map fragment and data observers.
     */
    @SuppressLint("MissingPermission")
    private fun setupMap() {
        // get a reference to the map fragment
        val mapFrag = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        try {
            MapsInitializer.initialize(requireActivity().applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // get a reference to the map object when ready
        mapFrag!!.getMapAsync { googleMap ->
            map = googleMap
            sheetBehavior.addBottomSheetCallback(
                SheetCallback(
                    map,
                    sheetBehavior
                )
            )

            // For showing a move to my location button
            map.isMyLocationEnabled = true

            // set zoom restrictions for performance
            map.setMinZoomPreference(12f)
            map.setMaxZoomPreference(16f)

            // get the device location
            locutil.getLocation { location ->
                setupCamera(location)

                // observe the products from the view model
                viewModel.getProducts().observe(viewLifecycleOwner, productsObserver)

                // update the products when the camera stops moving
                map.setOnCameraIdleListener {
                    updateProducts()
                }
                // hide seller names when zoom < 14.3
                map.setOnCameraMoveListener {
//                    if (map.cameraPosition.zoom >= 14.3) {
//                        hideSellerNames()
//                    } else {
//                        showSellerNames()
//                    }

                    for (marker in markers) {
                        when (marker.type) {
                            TYPE_SELLER -> {
                                marker.isVisible = map.cameraPosition.zoom >= 14.3
                            }
                        }
                    }
                }
                // set on marker click listener
                map.setOnMarkerClickListener { marker ->
                    onMarkerClick(marker)
                    return@setOnMarkerClickListener !debug
                }
                map.setOnMapClickListener {
                    sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
                map.setOnPolygonClickListener {
                    sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        }
    }

    /**
     * Handles marker clicks.
     */
    private fun onMarkerClick(marker: Marker) {
        selectedMarker = marker
        val geo = marker.snippet

        selectedProducts.clear()
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        selectedProducts.addAll(productsMap[geo]!! as ArrayList<Parcelable>)

        when (marker.type) {
            TYPE_CENTER -> {
                onCenterClick()
            }
            TYPE_SELLER -> {
                onSellerClick(geo)
            }
            else -> {
//                val prods = productsMap[geo]!!
//                for (p in prods) {
//                    if (p.unit == marker.type)
//                        selectedProducts.add(p)
//                }
//
//                val fragment: ProductListFragment? =
//                    childFragmentManager.findFragmentById(R.id.products_list) as ProductListFragment?
//                fragment!!.products =
//                    selectedProducts.toList() as List<Product>
//                sheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }
        }

        // center camera on marker
        map.animateCamera(CameraUpdateFactory.newLatLng(marker.position))
    }

    /**
     * Handles icon click.
     * Populates the [product list][ProductListFragment] and half expands the [bottom sheet][bottomSheet].
     */
    private fun onCenterClick() {
        val fragment: ProductListFragment? = childFragmentManager.findFragmentById(R.id.products_list) as ProductListFragment?
        fragment!!.products = selectedProducts.toList() as List<Product>
        sheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    /**
     * Handles seller name click.
     * Starts [UserActivity] for given user.
     */
    private fun onSellerClick(geo: String) {
        val intent = Intent(
            requireContext(),
            UserActivity::class.java
        )
        repo.getUserProfileCache(productsMap[geo]!![0].sellerUid!!)
            .addOnSuccessListener {
                intent.putExtra(UserActivity.ARG_USER, it as Parcelable)
                startActivity(intent)
            }
    }

    /**
     * Set the camera's starting position and zoom based on the given start values or the defaults.
     */
    private fun setupCamera(location: GeoPoint) {
        // setup map camera
        val newPoint = if(startGeohash == null) { //current location or given starting point
            location
        } else {
            Geohash.geohashToCoords(startGeohash!!)
        }

        if (startDistance == null)
            startDistance = 2
        val zoom = 20 - (startDistance!! * 5f)

        val latlng = LatLng(newPoint.latitude, newPoint.longitude)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15f))
    }

    /**
     * Called when the products from the view model are updated.
     */
    private fun onProductsMapUpdate(productsMap: HashMap<String, List<Product>>) {
        this.productsMap.putAll(viewModel.newestProducts)
        for ((geohash, products) in viewModel.newestProducts) {
            drawGeohash(geohash, products)
        }
    }

    /**
     * Inform the view model that the products need refreshing.
     * Called when the user has moved the camera.
     */
    private fun updateProducts() {
        viewModel.updateProducts(GeoPoint(map.projection.visibleRegion.nearLeft.latitude, map.projection.visibleRegion.nearLeft.longitude), GeoPoint(map.projection.visibleRegion.farRight.latitude, map.projection.visibleRegion.farRight.longitude))
    }

    /**
     * Draws an icon for the geohash if it contains products.
     */
    private fun drawGeohash(geohash: String, products: List<Product>) {
        val bounds = Geohash.getBounds(geohash)
//        val col = if(products.isEmpty())
//            clearColor
//        else
//            productsColor
//
//        if(mMap.cameraPosition.zoom > 12) {
//            val poly = mMap.addPolygon(
//                PolygonOptions()
//                    .clickable(true)
//                    .fillColor(col)
//                    .strokeColor(strokeColor)
//                    .strokeWidth(3f)
//                    .strokePattern(listOf(Dash(30F), Gap(80F)))
//                    .add(LatLng(bounds[0], bounds[1]))//bottom left
//                    .add(LatLng(bounds[0], bounds[3]))//bottom right
//                    .strokePattern(listOf(Dash(30F), Gap(40F)))
//                    .add(LatLng(bounds[2], bounds[3]))//top right
//                    .add(LatLng(bounds[2], bounds[1]))//top left
//            )
//            grid.add(poly)
//        } else {
//            for(gr in grid) {
//                gr.isVisible = false
//            }
//        }

        if(products.isNotEmpty()) {
//            val numOfTypes = 3
//            val distanceFromCenter = 125
//            var num = 0
//            val center = mMap.projection.toScreenLocation(LatLng(
//                bounds[0] + Geohash.LAT_PER_GEOHASH * 0.65,
//                bounds[1] + Geohash.LON_PER_GEOHASH / 2
//            ))
//
//            // add the "dairy" type marker
//            var angle = toRadians((180 / numOfTypes * num++).toDouble())
//            mMap.addMarker(
//                MarkerOptions().position(
//                    mMap.projection.fromScreenLocation(Point(
//                        (center.x + cos(angle) * distanceFromCenter).toInt(),
//                        (center.y + sin(angle) * distanceFromCenter).toInt()
//                        )
//                    )
//                )
//                    .flat(true)
//                    .anchor(0.5f, 0.5f)
//                    .title("" + products.size + " products")
//                    .snippet(geohash)
//                    .icon(getIconWithImage(R.drawable.ic_round))
//            ).type = Product.TYPE_DAIRY
//
//            // add the "vegetables" type marker
//            angle = toRadians((180 / numOfTypes * num++).toDouble())
//            mMap.addMarker(
//                MarkerOptions().position(
//                    mMap.projection.fromScreenLocation(Point(
//                        (center.x + cos(angle) * distanceFromCenter).toInt(),
//                        (center.y + sin(angle) * distanceFromCenter).toInt()
//                    )
//                    )
//                )
//                    .flat(true)
//                    .anchor(0.5f, 0.5f)
//                    .title("" + products.size + " products")
//                    .snippet(geohash)
//                    .icon(getIconWithImage(R.drawable.ic_round))
//            ).type = Product.TYPE_VEGETABLES
//
//            // add the "fruit" type marker
//            angle = toRadians((180 / numOfTypes * num++).toDouble())
//            mMap.addMarker(
//                MarkerOptions().position(
//                    mMap.projection.fromScreenLocation(Point(
//                        (center.x + cos(angle) * distanceFromCenter).toInt(),
//                        (center.y + sin(angle) * distanceFromCenter).toInt()
//                    )
//                    )
//                )
//                    .flat(true)
//                    .anchor(0.5f, 0.5f)
//                    .title("" + products.size + " products")
//                    .snippet(geohash)
//                    .icon(getIconWithImage(R.drawable.ic_chicken))
//            ).type = Product.TYPE_FRUIT
//
//            // add the "plants" type marker
//            angle = toRadians((180 / numOfTypes * num++).toDouble())
//            mMap.addMarker(
//                MarkerOptions().position(
//                    mMap.projection.fromScreenLocation(Point(
//                        (center.x + cos(angle) * distanceFromCenter).toInt(),
//                        (center.y + sin(angle) * distanceFromCenter).toInt()
//                    )
//                    )
//                )
//                    .flat(true)
//                    .anchor(0.5f, 0.5f)
//                    .title("" + products.size + " products")
//                    .snippet(geohash)
//                    .icon(getIconWithImage(R.drawable.ic_chicken))
//            ).type = Product.TYPE_PLANTS

            // if the geohash contains products from a featured seller, add the seller's name as a marker to the grid
            if(products[0].featured!!) {
                val m = map.addMarker(
                    MarkerOptions().position(
                        LatLng(
                            bounds[0] + Geohash.LAT_PER_GEOHASH,
                            bounds[1] + Geohash.LON_PER_GEOHASH / 2
                        )
                    )
                        .flat(true)
                        .anchor(0.5f, -3f)
                        .title("" + products.size + " products")
                        .snippet(geohash)
                        .icon(IconUtils.getIconWithText(requireContext(), products[0].sellerName!!))
                )
                m.type = TYPE_SELLER
                markers.add(m)
                m.isVisible = map.cameraPosition.zoom >= 14.3
            }


            // add center marker to display the number of products in the geohash
            map.addMarker(
                MarkerOptions().position(
                    LatLng(
                        bounds[0] + Geohash.LAT_PER_GEOHASH * 0.95,
                        bounds[1] + Geohash.LON_PER_GEOHASH / 2
                    )
                )
                    .flat(true)
                    .anchor(0.5f, 0.05f)
                    .title("" + products.size + " products")
                    .snippet(geohash)
                    .icon(IconUtils.getIconWithNumbers(requireContext(), products.size))
                    .zIndex(2.0f)
            ).type = TYPE_CENTER
        }
    }

    override fun onMarkerDragEnd(p0: Marker?) {
        TODO("Not yet implemented")
    }

    override fun onMarkerDragStart(p0: Marker?) {
        TODO("Not yet implemented")
    }

    override fun onMarkerDrag(p0: Marker?) {
        TODO("Not yet implemented")
    }

    /**
     * The callback for the bottom sheet.
     * Used to control its state and adjust the map fragment dimensions accordingly.
     */
    private class SheetCallback(mMap: GoogleMap, behavior: BottomSheetBehavior<LinearLayout>) : BottomSheetBehavior.BottomSheetCallback() {
        companion object {
            var selectedMarker: Marker? = null
        }

        private var mMap: GoogleMap = mMap
        private var behavior = behavior

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            mMap.setPadding(0, 0, 0, ((bottomSheet.parent as View).height - bottomSheet.top))
//            Log.d("XXXXXXXXXx", "padding: " + ((bottomSheet.parent as View).height - bottomSheet.top))
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_HIDDEN -> {
                    mMap.setPadding(0, 0, 0, 0)
                }
                BottomSheetBehavior.STATE_COLLAPSED -> {
                    mMap.setPadding(0, 0, 0, 0)
                }
                BottomSheetBehavior.STATE_DRAGGING -> {
                    mMap.setPadding(0, 0, 0, ((bottomSheet.parent as View).height - behavior.peekHeight))
                }
                BottomSheetBehavior.STATE_EXPANDED -> {
//                    mMap.setPadding(0, 0, 0, ((bottomSheet.parent as View).height - behavior.peekHeight))
                }
                BottomSheetBehavior.STATE_HALF_EXPANDED -> {
//                    mMap.setPadding(0, 0, 0, ((bottomSheet.parent as View).height - behavior.peekHeight))
                }
                BottomSheetBehavior.STATE_SETTLING -> {
                    mMap.setPadding(0, 0, 0, ((bottomSheet.parent as View).height - behavior.peekHeight))
                }
            }

        }

    }
}
