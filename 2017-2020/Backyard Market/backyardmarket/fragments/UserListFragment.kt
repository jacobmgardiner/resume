package com.yoloapps.backyardmarket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.classes.Product
import com.yoloapps.backyardmarket.data.classes.UserProfile
import com.yoloapps.backyardmarket.views.UserView
import com.yoloapps.backyardmarket.views.UserViewSmall


/**
 * A fragment with a recycler view that contains a list of users.
 */
class UserListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    var scrolling: Boolean = true
        set(value) {
            field = value
            recyclerView.isNestedScrollingEnabled = value
        }

    var orientation: Int =
        ORIENTATION_VERTICAL
        set(value) {
            if(value != field) {
                field = value
                onOrientationChange(value)
            }
        }

    private fun onOrientationChange(new: Int) {
        when(new) {
            ORIENTATION_VERTICAL -> {
                (recyclerView.layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.VERTICAL
            }
            ORIENTATION_HORIZONTAL -> {
                (recyclerView.layoutManager as LinearLayoutManager).orientation = LinearLayoutManager.HORIZONTAL
            }
        }
    }

    var users: List<UserProfile>? = null
        set(value) {
            field = value
            if (recyclerView.adapter == null) {
                recyclerView.adapter = users?.let {
                    UserListAdapter()
                }
            } else {
                recyclerView.adapter!!.notifyDataSetChanged()
            }
            orientation = orientation
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            users = it.getParcelableArray(ARG_USERS)!!.toList() as List<UserProfile>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.user_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = users?.let {
            UserListAdapter()
        }
    }

    override fun onResume() {
        super.onResume()
        (recyclerView.layoutManager as LinearLayoutManager).orientation = orientation
    }

    companion object {
        const val ORIENTATION_VERTICAL = LinearLayoutManager.VERTICAL
        const val ORIENTATION_HORIZONTAL = LinearLayoutManager.HORIZONTAL

        const val ARG_USERS = "users"

        @JvmStatic
        fun newInstance(products: List<Product>) =
            ProductListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArray(ARG_USERS, products.toTypedArray())
                }
            }
    }

    private inner class UserListAdapter() : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

        /**
         * Provide a reference to the type of views that you are using (custom ViewHolder)
         */
        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            lateinit var userView: CardView

            init {
                when(orientation) {
                    LinearLayoutManager.VERTICAL -> {
                        userView = (v as UserView)
                    }
                    LinearLayoutManager.HORIZONTAL -> {
                        userView = (v as UserViewSmall)
                    }
                }
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            // Create a new view.
            val userView = when(orientation) {
                LinearLayoutManager.VERTICAL -> {
//                    UserView(parent.context)
//                    LayoutInflater.from(parent.context).inflate(R.layout.user_view, parent, false) as UserView
                    CardView.inflate(parent.context, R.layout.user_view, UserView(parent.context)) as UserView
                }
                LinearLayoutManager.HORIZONTAL -> {
//                    UserViewSmall(parent.context)
                    CardView.inflate(parent.context, R.layout.user_view_small, UserViewSmall(parent.context)) as UserViewSmall
                }
                else -> UserView(parent.context)
            }

            if(userView is UserView)
            userView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return ViewHolder(userView)
        }

        override fun getItemCount(): Int {
            return users!!.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // Get element from your dataset at this position and replace the contents of the view
            // with that element

            when(orientation) {
                LinearLayoutManager.VERTICAL -> {
                    (holder.userView as UserView).user = users!![position]
                }
                LinearLayoutManager.HORIZONTAL -> {
                    (holder.userView as UserViewSmall).user = users!![position]
                }
            }

//            if (isImagePosition(position)) {
//                val url: String = urls.get(position)
//                Glide.with(fragment)
//                    .load(url)
//                    .into(holder.imageView)
//            } else {
//                Glide.with(fragment).clear(holder.imageView)
//                holder.imageView.setImageDrawable(specialDrawable)
//            }
        }

    }
}
