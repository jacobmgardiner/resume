package com.yoloapps.backyardmarket.activities

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.hardware.Camera
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.yoloapps.backyardmarket.CameraPreview
import com.yoloapps.backyardmarket.R
import java.io.FileNotFoundException
import java.io.IOException

class CameraActivity : AppCompatActivity() {
    companion object {
        const val PERMISSION_REQUEST_CAMERA = 1
    }
    private var camera: Camera? = null
    private var preview: CameraPreview? = null
    private var uri: Uri? = null

    private lateinit var coordinator: CoordinatorLayout

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_REQUEST_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setupCamera()
                } else {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
//                    Toast.makeText(this, "Please allow backyard market to access the camera.", Toast.LENGTH_LONG).show()
                    coordinator = findViewById(R.id.coordinator)
                    Snackbar.make(coordinator, "Please allow backyard market to access the camera.", Snackbar.LENGTH_LONG)
                        .setAction("Allow") { ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                            PERMISSION_REQUEST_CAMERA
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uri = intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT)
        setContentView(R.layout.activity_camera)

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
                PERMISSION_REQUEST_CAMERA
            )
        } else {
            setupCamera()
        }
    }

    private fun setupCamera() {
        camera = getCameraInstance()!!

        preview = camera?.let {
            // Create our Preview view
            CameraPreview(this, it)
        }

        // Set the Preview view as the content of our activity.
        preview?.also {
            val preview: FrameLayout = findViewById(R.id.camera_preview)
            preview.addView(it)
        }

        val captureButton: Button = findViewById(R.id.button_capture)
        captureButton.setOnClickListener {
            // get an image from the camera
            camera?.takePicture(null, null, Camera.PictureCallback { data, _ ->
                if(uri!=null) {
                    try {
                        val cos = contentResolver.openOutputStream(uri!!)!!
                        cos.write(data)
                        cos.close()
                    } catch (e: FileNotFoundException) {
                        Log.d("XXXXXXXXX", "File not found: ${e.message}")
                    } catch (e: IOException) {
                        Log.d("XXXXXXXXXX", "Error accessing file: ${e.message}")
                    }

                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releaseCamera()
    }

    override fun onPause() {
        super.onPause()
        releaseCamera()
    }

    private fun releaseCamera() {
        camera?.release() // release the camera for other applications
        camera = null
    }

    /** A safe way to get an instance of the Camera object. */
    private fun getCameraInstance(): Camera? {
        return try {
            Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
            null // returns null if camera is unavailable
        }
    }
}
