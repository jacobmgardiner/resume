package com.yoloapps.backyardmarket.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.firebase.firestore.GeoPoint
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.classes.Product
import com.yoloapps.backyardmarket.utils.LocationUtils
import com.yoloapps.location.utils.Geohash
import kotlinx.android.synthetic.main.location_fragment.*

class LocationFragment : Fragment() {
    var geohash: String? = null
        set(value) {
            field = value
            setupMap()
        }
    var distance: Int? = 3
    var product: Product? = null
        set(value) {
            field = value
            setupMap()
        }

    private lateinit var mMap: GoogleMap

    private lateinit var rootView: View

    private var fillColor = 0
    private var strokeColor = 0

    private lateinit var distanceText: TextView
    private lateinit var location: TextView

    private lateinit var description: TextView
    private lateinit var radioGroup: RadioGroup

    private lateinit var info: LinearLayout
    private lateinit var options: LinearLayout

    private val locutil by lazy { LocationUtils.getInstance(requireContext().applicationContext) }

    val selectedLocationOption: Int
        get() {
            return when(radioGroup.checkedRadioButtonId) {
                R.id.location -> {
                    0
                }
                R.id.zip -> {
                    1
                }
                else -> 0
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            geohash = it.getString(ARG_GEOHASH)
            distance = it.getInt(ARG_DISTANCE)
            product = it.getParcelable(ARG_PRODUCT)

            if(geohash == null && product != null) {
                geohash = product!!.g
            }
        }

        fillColor = Color.argb(100, Color.red(ContextCompat.getColor(requireContext(), R.color.colorPrimary)), Color.green(ContextCompat.getColor(requireContext(), R.color.colorPrimary)), Color.blue(ContextCompat.getColor(requireContext(), R.color.colorPrimary)))
        strokeColor = Color.argb(200, Color.red(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)), Color.green(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)), Color.blue(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)))
    }

    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_LOCATION
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setupMap()
                } else {
                    //TODO show blank map
                }
            }
            else -> {

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.location_fragment, container, false)

        if(checkPermission())
            setupMap()
        else
            requestPermission()

        distanceText = rootView.findViewById(R.id.distance)
        location = rootView.findViewById(R.id.city)

        description = rootView.findViewById(R.id.description)
        radioGroup = rootView.findViewById(R.id.radio_group)

        info = rootView.findViewById(R.id.seller_info)
        options = rootView.findViewById(R.id.options)

        return rootView
    }

    private fun setupMap() {
        if((geohash != null || product != null) && this::rootView.isInitialized) {
            val mapFrag = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
            try {
                MapsInitializer.initialize(requireActivity().applicationContext)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            mapFrag!!.getMapAsync(OnMapReadyCallback { googleMap ->
                mMap = googleMap

                mMap = googleMap

                mMap.uiSettings.setAllGesturesEnabled(false)

                val geopoint = when {
                    product != null -> {
                        product?.g?.let { Geohash.geohashToCoordsCenter(it) }
                    }
                    geohash != null -> {
                        geohash?.let { Geohash.geohashToCoordsCenter(it) }
                    }
                    else -> {
                        null
                    }
                }

                if (geopoint != null) {
                    val pos = LatLng(geopoint.latitude, geopoint.longitude)

                    //add marker
                    mMap.addMarker(MarkerOptions().position(pos))

                    //add rectangle
                    val bounds = Geohash.getBounds(geohash!!)
                    mMap.addPolygon(
                        PolygonOptions()
                            .clickable(true)
                            .fillColor(fillColor)
                            .strokeColor(strokeColor)
                            .strokeWidth(3f)
                            .strokePattern(listOf(Dash(30F), Gap(80F)))
                            .add(LatLng(bounds[0], bounds[1]))//bottom left
                            .add(LatLng(bounds[0], bounds[3]))//bottom right
                            .strokePattern(listOf(Dash(30F), Gap(40F)))
                            .add(LatLng(bounds[2], bounds[3]))//top right
                            .add(LatLng(bounds[2], bounds[1]))//top left
                    )

                    //move to location
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, toZoom(distance)))

                    if (product != null) {
                        initInfo(pos, geopoint)
                    } else {
                        initOptions(pos, geopoint)
                    }
                }
            })
        }
    }

    private fun initInfo(pos: LatLng, geopoint: GeoPoint) {
        info.visibility = View.VISIBLE

        location.text = locutil.cleanProductLocation(product?.city!!, Product.CITY) ?: "null"

        //display distance
        locutil.distanceTo(geopoint) {
            distanceText.text = it.toInt().toString()
        }
    }

    private fun initOptions(pos: LatLng, geopoint: GeoPoint) {
        options.visibility = View.VISIBLE

        locutil.getLocation {
            location.text = locutil.cleanProductLocation(locutil.getLocationAsString(requireContext(), it)[LocationUtils.Companion.StringLocation.CITY.ordinal], Product.CITY)
        }

        description.text = resources.getStringArray(R.array.location_settings_messages)[0]

        radioGroup.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            when(i) {
                R.id.location -> {
                    description.text = resources.getStringArray(R.array.location_settings_messages)[0]
                }
                R.id.zip -> {
                    description.text = resources.getStringArray(R.array.location_settings_messages)[1]
                }
            }
        }
    }

    private fun toZoom(distance: Int?): Float {
        return if (distance != null) {
            DEFAULT_ZOOM
        } else {
            DEFAULT_ZOOM
        }
    }

    companion object {
        private const val ARG_GEOHASH = "geohash"
        private const val ARG_DISTANCE = "distance"
        private const val ARG_PRODUCT = "product"

        private const val PERMISSION_REQUEST_LOCATION = 1

        private const val DEFAULT_ZOOM = 13F

        @JvmStatic
        fun newInstance(product: Product, geohash: String, distance: Int) =
            LocationFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PRODUCT, product)
                    putString(ARG_GEOHASH, geohash)
                    putInt(ARG_DISTANCE, distance)
                }
            }

        @JvmStatic
        fun newInstance(product: Product) =
            LocationFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PRODUCT, product)
                }
            }

        @JvmStatic
        fun newInstance(geohash: String, distance: Int) =
            LocationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_GEOHASH, geohash)
                    putInt(ARG_DISTANCE, distance)
                }
            }

        @JvmStatic
        fun newInstance(geohash: String) =
            LocationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_GEOHASH, geohash)
//                    putInt(ARG_DISTANCE, distance)
                }
            }
    }
}
