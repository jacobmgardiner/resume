package com.yoloapps.backyardmarket.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.snackbar.Snackbar
import com.yoloapps.backyardmarket.data.FirebaseStorageRepository
import com.yoloapps.backyardmarket.views.ImageEditView
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.classes.UserProfile
import com.yoloapps.backyardmarket.utils.TimestampUtils
import java.io.File
import java.io.IOException


class PrimaryImageEditActivity : AppCompatActivity() {
    companion object {
        const val ARG_USER = "user"

        const val PROFILE_CAPTURE = 1
        const val PROFILE_CHOOSE = 2
        const val OTHER_CAPTURE = 3
        const val OTHER_CHOOSE = 4
    }

    private lateinit var editView: ImageEditView
    private lateinit var new: Button
//    private lateinit var choose: Button
    private lateinit var coordinator: CoordinatorLayout

    private lateinit var user: UserProfile
    private var profileUri: Uri? = null

//    private var timestamp: String? = null
    private var newReference: String? = null

    private val repo by lazy { FirestoreRepository.getInstance(applicationContext) }

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
        setContentView(R.layout.activity_primary_image_edit)
        user = intent.getParcelableExtra(ARG_USER)

        setSupportActionBar(findViewById(R.id.transparent_toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
//        supportActionBar!!.title = product.title

        editView = findViewById(R.id.edit_view)
        user.primaryImage?.let {
            FirebaseStorageRepository.loadBitmap(this, it) { bitmap ->
                editView.bitmap = bitmap
            }
        }

        new = findViewById(R.id.new_image)
        new.setOnClickListener {
            //TODO bottom sheet
            newProfile()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            CameraActivity.PERMISSION_REQUEST_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startCamera()
                } else {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
//                    Toast.makeText(this, "Please allow backyard market to access the camera.", Toast.LENGTH_LONG).show()
                    coordinator = findViewById(R.id.coordinator)
                    Snackbar.make(coordinator, "Please allow backyard market to access the camera.", Snackbar.LENGTH_LONG)
                        .setAction("Allow") { ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                            CameraActivity.PERMISSION_REQUEST_CAMERA
                        ) }.show()
                    return
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    private fun startCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    profileUri = FileProvider.getUriForFile(
                        this,
                        "com.yoloapps.backyardmarket.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, profileUri)
                    startActivityForResult(takePictureIntent,
                        PROFILE_CAPTURE
                    )
                }
            }
        }
    }

    private fun newProfile() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
//                // Show an explanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//            } else {
//                // No explanation needed, we can request the permission.
//                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), PERMISSION_REQUEST_CAMERA)
//            }
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                CameraActivity.PERMISSION_REQUEST_CAMERA
            )
        } else {
            startCamera()
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val timestamp = TimestampUtils.timestamp
        newReference = user.newPrimaryReference(timestamp)
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timestamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            //currentPhotoPath = absolutePath
        }
    }

    fun chooseProfile() {
        val getIntent = Intent(Intent.ACTION_GET_CONTENT)
        getIntent.type = "image/*"

        val pickIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(getIntent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(pickIntent))

        startActivityForResult(chooserIntent,
            PROFILE_CHOOSE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PROFILE_CAPTURE && resultCode == RESULT_OK) {
//            imageProfile.setImageURI(profileUri)
//            val imageUri = data!!.data

            editView.bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                profileUri?.let {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.contentResolver, it))
                }
            } else {
                MediaStore.Images.Media.getBitmap(this.contentResolver, profileUri)
            }
        }
        else if (requestCode == PROFILE_CHOOSE && resultCode == RESULT_OK) {
            profileUri = data!!.data
//            imageProfile.setImageURI(profileUri)

            editView.bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                profileUri?.let {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.contentResolver, it))
                }
            } else {
                MediaStore.Images.Media.getBitmap(this.contentResolver, profileUri)
            }
        }
//        else if (requestCode == OTHER_CHOOSE && resultCode == RESULT_OK) {
//            if(otherUri != null) {
//                photoUris.add(otherUri!!)
//                addImageView(otherUri!!)
//            }
//        }
//        else if (requestCode == OTHER_CAPTURE && resultCode == RESULT_OK) {
//            if(otherUri != null) {
//                photoUris.add(otherUri!!)
//                addImageView(otherUri!!)
//            }
//        }
    }

    fun cancel(view: View) {
        finish()
    }

    fun save(view: View) {
        val oldReference = user.primaryImage
        user.primaryImage = newReference
        user.primaryImageCenter = listOf(editView.centerX, editView.centerY)
        user.primaryImageRadius = editView.radius.toFloat()
        repo.updateUserProfile(user)

        profileUri?.let { uri ->
            FirebaseStorageRepository.uploadImageWithThumbnail(user.primaryImage!!, editView.bitmap!!) {
                    if (oldReference != null) {
                        FirebaseStorageRepository.deleteImage(oldReference) {
                            finish()
                        }
                    }
            }
        }
    }
}
