package com.yoloapps.backyardmarket.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.FirebaseStorageRepository
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.fragments.*
import com.yoloapps.backyardmarket.models.SellViewModel
import com.yoloapps.backyardmarket.utils.LocationUtils
import com.yoloapps.backyardmarket.utils.TimestampUtils
import com.yoloapps.location.utils.Geohash
import kotlinx.android.synthetic.main.price_selection_fragment.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class SellActivity : AppCompatActivity() {
    companion object {
        const val ARG_PRODUCT = "product"

        const val TAG = "XXXXXXX"
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    private lateinit var photoURI: Uri
    private lateinit var currentPhotoPath: String
    private lateinit var viewModel: SellViewModel
    private lateinit var image: ImageView
    private lateinit var coordinator: CoordinatorLayout
//    private var unit: Int = 0

    private var priceReady = false
    private var photoReady = false

    private lateinit var thumb: Bitmap

    private lateinit var textFrag: ProductTextInputFragment
    private lateinit var locationFrag: LocationFragment
    private lateinit var categoryFrag: CategorySelectionFragment
    private lateinit var priceFrag: PriceSelectionFragment
    private lateinit var typeFrag: TypeSelectionFragment

    private val repo by lazy { FirestoreRepository.getInstance(applicationContext) }
    private val locutils by lazy { LocationUtils.getInstance(applicationContext) }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() //Call the back button's method
                return true
            }
            else -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repo.getIsConnectedToStripe {
            Log.d("XXXXXXXX", "account: $it")
            if (!it) {
                Log.d("XXXXXXXX", "account not setup!")
                startActivity(Intent(this, StripeSetupActivity::class.java))
                return@getIsConnectedToStripe
            }
        }
            .addOnCompleteListener {
                Log.d("XXXXXXXX", it.result?.data.toString())
                Log.e("XXXXXXXX", "err: "+it.exception?.message)
            }

        setContentView(R.layout.activity_sell)

        setSupportActionBar(findViewById(R.id.transparent_toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        supportActionBar!!.title = product.title

        textFrag = supportFragmentManager.findFragmentById(R.id.text_fragment)!! as ProductTextInputFragment

        locationFrag = supportFragmentManager.findFragmentById(R.id.location)!! as LocationFragment
        locutils.getLocation {
            locationFrag.geohash = Geohash.geohash(it.latitude, it.longitude) /*{ locationFrag.geohash = it }*/
        }

        categoryFrag = supportFragmentManager.findFragmentById(R.id.category_fragment)!! as CategorySelectionFragment
        categoryFrag.onSelection = { category ->
            if (category == null) {
                typeFrag.collapse()
            } else {
                typeFrag.expand(category)
            }
        }

        priceFrag = supportFragmentManager.findFragmentById(R.id.price_fragment)!! as PriceSelectionFragment

        typeFrag = supportFragmentManager.findFragmentById(R.id.type_fragment)!! as TypeSelectionFragment
        typeFrag.onSelection = { type ->
            if (type == null) {
                priceFrag.collapse()
            } else {
                priceFrag.expand(type)
            }
        }

        viewModel = ViewModelProvider(this).get(SellViewModel::class.java)

        image = findViewById(R.id.image)
        coordinator = findViewById(R.id.coordinator)
    }

    fun cancel(view: View) {
        finish()
    }

    /** Check if this device has a camera */
    private fun checkCameraHardware(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
        } else {
//            TODO("VERSION.SDK_INT < JELLY_BEAN_MR1")
//            context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
            true
        }
    }


    fun sell(view: View) {
        if(photoReady) {
            if(textFrag.title != null && textFrag.title!!.length > 1) {
                if(priceFrag.selectedPrice != null) {
                    val auth = FirebaseAuth.getInstance()
                    val user = auth.currentUser
                    Log.d(TAG, "starting sell")
                    viewModel.sell(
                        this,
                        this,
                        categoryFrag.selectedCategory!!,
                        typeFrag.selectedType!!,
                        priceFrag.selectedUnit!!,
                        number.value,
                        (dollars.text.toString() + "." + cents.text.toString()),
                        total.value,
                        textFrag.title!!,
                        textFrag.description!!,
                        user!!.uid,
                        TimestampUtils.timestamp,
                        photoURI,
                        thumb,
                        locationFrag.selectedLocationOption,
                        {
                            //TODO either speed up selling and make load screen or exit before finished
                            //finish()
                            startActivity(Intent(this, MainActivity::class.java))
                        }, {
//                            Toast.makeText(this, "Error uploading product.", Toast.LENGTH_SHORT)
//                                .show()
                            Snackbar.make(coordinator, "Error uploading product!", Snackbar.LENGTH_LONG).show()
                        }
                    )
                } else {
//                    Toast.makeText(this, "Must finish setting up the product's price", Toast.LENGTH_LONG).show()
                    Snackbar.make(coordinator, "Must finish setting up the product's price", Snackbar.LENGTH_LONG).show()
                }
            } else {
//                Toast.makeText(this, "Must create a title for the product", Toast.LENGTH_LONG).show()
                Snackbar.make(coordinator, "Must create a title for the product", Snackbar.LENGTH_LONG).show()
            }
        } else {
//            Toast.makeText(this, "Must take a photo of the product", Toast.LENGTH_LONG).show()
            Snackbar.make(coordinator, "Must create a title for the product", Snackbar.LENGTH_LONG).show()
        }
    }

    fun camera(view: View) {
        val cameraIntent = Intent(this, CameraActivity::class.java)
        // Create the File where the photo should go
        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            // Error occurred while creating the File
            null
        }
        // Continue only if the File was successfully created
        photoFile?.also {
            photoURI = FileProvider.getUriForFile(this, "com.yoloapps.backyardmarket.fileprovider", it)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(cameraIntent,
                REQUEST_IMAGE_CAPTURE
            )
        }

    }

    fun notListed(view: View) {
        //TODO send an email or something
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode != RESULT_CANCELED) {
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                image.setImageURI(photoURI)
                photoReady = true

                thumb = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    val cr = applicationContext.contentResolver
                    cr.loadThumbnail(
                        photoURI, Size(
                            FirebaseStorageRepository.THUMBSIZE,
                            FirebaseStorageRepository.THUMBSIZE
                        ), null
                    )
                } else {
                    MediaStore.Images.Media.getBitmap(this.contentResolver, photoURI)
                }
            }
        }
    }
}
