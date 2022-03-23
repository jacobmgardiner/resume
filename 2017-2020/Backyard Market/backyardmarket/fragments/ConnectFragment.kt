package com.yoloapps.backyardmarket.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.models.ConnectViewModel

class ConnectFragment : Fragment() {

    companion object {
        fun newInstance() =
            ConnectFragment()

    }

    private lateinit var viewModel: ConnectViewModel
    private lateinit var root: ViewGroup

    private lateinit var followingFrag: FollowingFragment
    private lateinit var featuredFrag: FeaturedFragment
//    private lateinit var localFrag: FeaturedFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.connect_fragment, container, false) as ViewGroup

        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        //TODO local pages?

        followingFrag = childFragmentManager.findFragmentById(R.id.following_fragment) as FollowingFragment
        followingFrag.user = uid

        featuredFrag = childFragmentManager.findFragmentById(R.id.sellers_fragment) as FeaturedFragment
        featuredFrag.user = uid
//        featuredFrag.max = 5

//        localFrag = childFragmentManager.findFragmentById(R.id.local_sellers_fragment) as FeaturedFragment
//        localFrag.user = uid

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun setMenuVisibility(visible: Boolean) {
        super.setMenuVisibility(visible)
        if (visible) {
            if (activity != null) {
                (activity as AppCompatActivity).supportActionBar?.title = "Connect"
            }
        }
    }
}
