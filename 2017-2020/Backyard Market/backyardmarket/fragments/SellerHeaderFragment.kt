package com.yoloapps.backyardmarket.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.activities.PrimaryImageEditActivity
import com.yoloapps.backyardmarket.activities.SecondaryImageEditActivity
import com.yoloapps.backyardmarket.data.FirebaseStorageRepository
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.classes.UserProfile
import com.yoloapps.backyardmarket.views.ProfileImageView


class SellerHeaderFragment : Fragment() {
    companion object {
        fun newInstance() =
            PriceSelectionFragment()
    }

//    private lateinit var viewModel: SellerHeaderViewModel

    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var secondaryImage: ImageView
    private lateinit var primaryImageParent: FrameLayout
    private lateinit var primaryImage: ProfileImageView
    private lateinit var nameView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var descriptionEditText: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var followersView: TextView
    private lateinit var primImageEdit: FloatingActionButton
    private lateinit var secImageEdit: FloatingActionButton
    private lateinit var descriptionEdit: FloatingActionButton
//    private lateinit var categoriesFrag: CategoriesFragment

    private var created = false
    private var editingDesc = false

    private val repo by lazy { FirestoreRepository.getInstance(requireContext().applicationContext) }

//    var user: String? = null
//        set(value) {
//            field = value
//            if(value != null) {
//                MyUtils.getUserData(value, requireActivity()) {
//                    userProfile = it
//                }
//            }
//        }

    var userProfile: UserProfile? = null
        set(value) {
            field = value
            if (userProfile != null) {
                initViews()
            }
        }

    var editable: Boolean = false
        set(value) {
            field = value
            if(userProfile != null) {
                initViews()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        created = true
        val root = inflater.inflate(R.layout.seller_header_fragment, container, false)

        constraintLayout = root.findViewById(R.id.constraint)
        secondaryImage = root.findViewById(R.id.sec_image)
        primaryImageParent = root.findViewById(R.id.prim_image_parent)
        nameView = root.findViewById(R.id.name_view)
        descriptionView = root.findViewById(R.id.description_view)
        descriptionEditText = root.findViewById(R.id.description_edit_text)
        ratingBar = root.findViewById(R.id.rating_view)
        followersView = root.findViewById(R.id.followers)

        primImageEdit = root.findViewById(R.id.prim_image_edit)
        primImageEdit.visibility = View.GONE

        secImageEdit = root.findViewById(R.id.sec_image_edit)
        secImageEdit.visibility = View.GONE

        descriptionEdit = root.findViewById(R.id.desc_edit)
        descriptionEdit.visibility = View.GONE

        return root
    }

    private fun initViews() {
        if(created) {
            if (userProfile?.secondaryImage != null) {
                FirebaseStorageRepository.loadImage(requireContext(), userProfile?.secondaryImage!!, secondaryImage)
            }

            primaryImage = ProfileImageView(
                requireContext(),
                userProfile!!,
                ProfileImageView.SIZE_LARGE
            )
            primaryImageParent.addView(primaryImage)
            userProfile?.secondaryImage?.let {
                FirebaseStorageRepository.loadImage(
                    requireContext(),
                    it,
                    secondaryImage
                )
            }

            nameView.text = userProfile!!.displayName
            descriptionView.text = userProfile!!.description ?: "No description"
            ratingBar.rating = userProfile!!.averageRating.toFloat()
            followersView.text = (userProfile!!.followers?.size ?: 0).toString()

            if (editable) {
                //TODO make editable

                primImageEdit.setOnClickListener {
                    val intent = Intent(requireContext(), PrimaryImageEditActivity::class.java)
                    intent.putExtra(PrimaryImageEditActivity.ARG_USER, userProfile as Parcelable)
                    requireContext().startActivity(intent)
                }
                primImageEdit.visibility = View.VISIBLE

                secImageEdit.setOnClickListener {
                    val intent = Intent(requireContext(), SecondaryImageEditActivity::class.java)
                    intent.putExtra(SecondaryImageEditActivity.ARG_USER, userProfile as Parcelable)
                    requireContext().startActivity(intent)
                }
                secImageEdit.visibility = View.VISIBLE

                descriptionEdit.setOnClickListener {
                    if (!editingDesc) {
                        val params = descriptionEdit.layoutParams as ConstraintLayout.LayoutParams
                        params.rightToRight = descriptionEditText.id
                        params.bottomToBottom = descriptionEditText.id
                        descriptionEdit.layoutParams = params

                        descriptionView.visibility = View.GONE
                        descriptionEditText.visibility = View.VISIBLE
//                        descriptionEditText = EditText(requireContext())
                        descriptionEditText.setText(userProfile!!.description, TextView.BufferType.EDITABLE)
                        descriptionEditText.addTextChangedListener {
                            userProfile!!.description = it.toString()
                        }
                        descriptionEdit.setImageResource(R.drawable.ic_chevron)
                        editingDesc = true
                    } else {
                        val params = descriptionEdit.layoutParams as ConstraintLayout.LayoutParams
                        params.rightToRight = descriptionView.id
                        params.bottomToBottom = descriptionView.id
                        descriptionEdit.layoutParams = params

                        descriptionView.visibility = View.VISIBLE
                        descriptionEditText.visibility = View.GONE
                        repo.updateUserProfile(userProfile!!)
//                        descriptionView = TextView(requireContext())
                        descriptionView.text = userProfile!!.description
                        descriptionEdit.setImageResource(R.drawable.ic_pencil)
                        editingDesc = false
                    }
                }
                descriptionEdit.visibility = View.VISIBLE
            }
        }
    }
}