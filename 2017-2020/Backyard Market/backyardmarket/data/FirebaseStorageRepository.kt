package com.yoloapps.backyardmarket.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.storage.FirebaseStorage
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.glide.Resize
import java.io.ByteArrayOutputStream
import java.io.IOException
import kotlin.math.min

object FirebaseStorageRepository {
    const val THUMBSIZE = 128
    const val THUMB_QUALITY = 300 //average of width/height
    const val IMAGE_QUALITY = 1000 //average of width/height
    const val THUMB = "th"

    const val DEFAULT_ERROR =
        R.drawable.ic_launcher_background
    const val DEFAULT_PLACEHOLDER =
        R.drawable.ic_round_chicken

    const val TAG_STORAGE_CALL = "StorageCall"
    private var callCount = 0
    fun callCount() {
        callCount++
        Log.d(TAG_STORAGE_CALL, "count: $callCount")
    }

    val storage by lazy { FirebaseStorage.getInstance() }

    fun loadImage(context: Context, imageReference: String, view: ImageView): Boolean {
        callCount()
        return try {
            val ref = storage
                .getReference(imageReference)
            Glide.with(context)
                .load(ref)
                .placeholder(DEFAULT_PLACEHOLDER)
                .error(DEFAULT_ERROR)
                .into(view)
            true
        } catch (e: IOException) {
            false
        }
    }

    fun loadImage(context: Context, imageReference: String, view: ImageView, placeholder: Int, error: Int): Boolean {
        callCount()
        return try {
            val ref = storage
                .getReference(imageReference)
            Glide.with(context)
                .load(ref)
                .placeholder(placeholder)
                .error(error)
                .into(view)
            true
        } catch (e: IOException) {
            false
        }
    }

    fun loadCircularImage(context: Context, imageReference: String, center: List<Double>, radius: Float, view: ImageView): Boolean {
        callCount()
        return try {
            val ref = storage
                .getReference(imageReference)
            Glide.with(context)
                .load(ref)
                .placeholder(DEFAULT_PLACEHOLDER)
                .error(DEFAULT_ERROR)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(MultiTransformation(Resize(center, radius), CircleCrop()))
                .into(view)
            true
        } catch (e: IOException) {
            false
        }
    }

    fun loadCircularImage(context: Context, imageReference: String, center: List<Double>, radius: Float, view: ImageView, placeholder: Int, error: Int): Boolean {
        callCount()
        return try {
            val ref = storage
                .getReference(imageReference)
            Glide.with(context)
                .load(ref)
                .placeholder(placeholder)
                .error(error)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transform(MultiTransformation(Resize(center, radius), CircleCrop()))
                .into(view)
            true
        } catch (e: IOException) {
            false
        }
    }

    fun loadBitmap(context: Context, imageReference: String, placeholder: Int, error: Int, onLoad: (Bitmap) -> Unit): Boolean {
        callCount()
        return try {
            val ref = storage
                .getReference(imageReference)
            Glide
                .with(context)
                .asBitmap()
                .placeholder(placeholder)
                .error(error)
                .load(ref)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        onLoad(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // this is called when imageView is cleared on lifecycle call or for
                        // some other reason.
                        // if you are referencing the bitmap somewhere else too other than this imageView
                        // clear it here as you can no longer have the bitmap
                        Log.d("XXXXXX", "CLEARED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                    }
                })
            true
        } catch (e: IOException) {
            false
        }
    }

    fun loadBitmap(context: Context, imageReference: String, onLoad: (Bitmap) -> Unit): Boolean {
        callCount()
        return try {
            val ref = storage
                .getReference(imageReference)
            Glide
                .with(context)
                .asBitmap()
                .placeholder(DEFAULT_PLACEHOLDER)
                .error(DEFAULT_ERROR)
                .load(ref)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        onLoad(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // this is called when imageView is cleared on lifecycle call or for
                        // some other reason.
                        // if you are referencing the bitmap somewhere else too other than this imageView
                        // clear it here as you can no longer have the bitmap
                        Log.d("XXXXXX", "CLEARED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
                    }
                })
            true
        } catch (e: IOException) {
            false
        }
    }

    fun uploadImage(ref: String, bitmap: Bitmap, success: () -> Unit) {
        callCount()
        //TODO resize dont compress
        val baos = ByteArrayOutputStream()
        val avgDimen = (bitmap.width + bitmap.height) / 2
        val quality = 100 * min(1, IMAGE_QUALITY / avgDimen)
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
        val data = baos.toByteArray()

        val uploadTask = storage
            .getReference(ref)
            .putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.e("XXXXXXXXXX", "COULDN'T UPLOAD IMAGE: "+ref)
            Log.e("XXXXXX", it.message)
        }.addOnSuccessListener {
            Log.d("XXXXXXXXXX", "UPLOADED IMAGE: "+ref)
            success()
        }
    }

    fun uploadImageWithThumbnail(ref: String, bitmap: Bitmap, success: () -> Unit) {
        callCount()
        uploadImage(
            ref,
            bitmap
        ) {
            uploadThumbnail(
                ref,
                bitmap,
                success
            )
        }
    }

    fun uploadThumbnail(ref: String, bitmap: Bitmap, success: () -> Unit) {
        callCount()
        //TODO resize dont compress
        val baos = ByteArrayOutputStream()
        val avgDimen = (bitmap.width + bitmap.height) / 2
        val quality = 100 * min(1, THUMB_QUALITY / avgDimen)
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
        val data = baos.toByteArray()

        val uploadTask2 = storage
            .getReference(ref + THUMB)
            .putBytes(data)
        uploadTask2.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.e("XXXXXXXXXX", "COULDN'T UPLOAD THUMB: "+ref)
            Log.e("XXXXXX", it.message)
        }.addOnSuccessListener {
            Log.d("XXXXXXXXXX", "UPLOADED THUMB: "+ref)
            success()
        }
    }

    fun deleteImage(ref: String, success: () -> Unit) {
        callCount()
        storage.getReference(ref)
            .delete()
            .addOnSuccessListener {
                Log.d("XXXXXXX", "DELETED IMAGE: " + ref)
                success()
            }
            .addOnFailureListener {
                Log.e("XXXXXXX", "COULDN'T DELETE IMAGE: " + ref)
                Log.e("XXXXXXXXXX", it.message)
            }

        storage.getReference(ref + THUMB)
            .delete()
            .addOnSuccessListener {
                Log.d("XXXXXXX", "DELETED THUMB: " + ref)
                success()
            }
            .addOnFailureListener {
                Log.e("XXXXXXX", "COULDN'T DELETE THUMB: " + ref)
                Log.e("XXXXXXXXXX", it.message)
            }
    }
}