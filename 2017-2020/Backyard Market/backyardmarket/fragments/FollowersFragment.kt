package com.yoloapps.backyardmarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.QuerySnapshot
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.classes.UserProfile
import com.yoloapps.backyardmarket.models.FollowersViewModel

class FollowersFragment : Fragment() {

    private lateinit var usersFrag: UserListFragment

    private lateinit var viewModel: FollowersViewModel

    private lateinit var emptyText: TextView
    private lateinit var fragContainer: LinearLayout
    private var empty = true

    var user: String? = null
        set(value) {
            field = value
            initUsersFrag()
        }

    var followers: List<UserProfile>? = null
        private set
        get() {
            return usersFrag.users
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.followers_fragment, container, false)

        fragContainer = view.findViewById(R.id.container)

        emptyText = view.findViewById(R.id.empty_text)

        usersFrag = childFragmentManager.findFragmentById(R.id.users_fragment) as UserListFragment
        usersFrag.orientation = UserListFragment.ORIENTATION_HORIZONTAL

        return view
    }

    private fun initUsersFrag() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(FollowersViewModel::class.java)
        val observer = Observer<HashMap<String, UserProfile>> { users ->
            usersFrag.users = users.values.toList()
            if(!empty && users.isEmpty()) {
                onEmpty(true)
            } else if(empty && users.isNotEmpty()) {
                onEmpty(false)
            }
        }
        viewModel.getFollowers(viewLifecycleOwner).observe(viewLifecycleOwner, observer)
    }

    private fun onEmpty(empty: Boolean) {
        this.empty = empty
        if(empty) {
            emptyText.visibility = View.VISIBLE
        } else {
            emptyText.visibility = View.GONE
        }
    }

    companion object {
        const val ARROW_EXPAND = R.drawable.ic_expand
        const val ARROW_COLLAPSE =
            R.drawable.ic_collapse

        const val ARG_BUYER = "buyer"

        @JvmStatic
        fun newInstance(/*buyer: String*/) =
            ProductListFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_BUYER, buyer)
                }
            }

        const val STATE_EXPANDED = 0
        const val STATE_COLLAPSED = 1
    }
}
