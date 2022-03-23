package com.yoloapps.backyardmarket.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.activities.SellActivity
import com.yoloapps.backyardmarket.data.classes.UserProfile
import com.yoloapps.backyardmarket.activities.StartActivity
import com.yoloapps.backyardmarket.activities.VerifyActivity
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.models.SellerDashboardViewModel

class SellerDashboardFragment : Fragment(){
    companion object {
        fun newInstance() =
            SellerDashboardFragment()
    }

    private lateinit var viewModel: SellerDashboardViewModel

    private lateinit var root: ViewGroup
    private lateinit var sellerFrag: SellerHeaderFragment
    private lateinit var tipsFrag: TipsFragment
    private lateinit var followersFrag: FollowersFragment
    private lateinit var buyingFrag: BuyingFragment
    private lateinit var bookmarksFrag: BookmarksFragment
    private lateinit var sellingFrag: SellingFragment
    private lateinit var postsFrag: PostsFragment

    private lateinit var newButton: FloatingActionButton

    private val repo by lazy { FirestoreRepository.getInstance(requireContext().applicationContext) }

    private fun checkIfLoggedIn(): Boolean {
        val user = FirebaseAuth.getInstance().currentUser
        return if (user == null) {
            val intent = Intent(requireContext(), StartActivity::class.java)
            startActivity(intent)
            false
        } else if (!user.isEmailVerified) {
            val intent = Intent(requireContext(), VerifyActivity::class.java)
            startActivity(intent)
            false
        } else {
            true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.seller_dashboard_fragment, container, false) as ViewGroup

        if(!checkIfLoggedIn())
            return null

        newButton = root.findViewById(R.id.new_button)
        newButton.setOnClickListener {
            //TODO give option in bottom sheet for product or post
            startActivity(Intent(context, SellActivity::class.java))
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val observer = Observer<DocumentSnapshot> { user ->
            val profile = user.toObject(UserProfile::class.java)

            sellerFrag = childFragmentManager.findFragmentById(R.id.seller_fragment) as SellerHeaderFragment
            sellerFrag.userProfile = profile
            sellerFrag.editable = true

            //TODO check if disabled
            tipsFrag = childFragmentManager.findFragmentById(R.id.tips_fragment) as TipsFragment
            tipsFrag.user = profile!!.uid

            followersFrag = childFragmentManager.findFragmentById(R.id.followers_fragment) as FollowersFragment
            followersFrag.user = profile.uid

            buyingFrag = childFragmentManager.findFragmentById(R.id.buying_fragment) as BuyingFragment
            buyingFrag.buyer = profile.uid

            bookmarksFrag = childFragmentManager.findFragmentById(R.id.bookmarks_fragment) as BookmarksFragment
            bookmarksFrag.user = profile.uid

            sellingFrag = childFragmentManager.findFragmentById(R.id.selling_fragment) as SellingFragment
            sellingFrag.user = profile.uid
        }
        repo.getUserProfile().observe(viewLifecycleOwner, observer)
    }

    override fun setMenuVisibility(visible: Boolean) {
        super.setMenuVisibility(visible)
        if (visible) {
            if (activity != null) {
                (activity as AppCompatActivity).supportActionBar?.title = "Dashboard"
            }
        }
    }
}