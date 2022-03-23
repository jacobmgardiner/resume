package com.yoloapps.backyardmarket.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.classes.UserProfile
import com.yoloapps.backyardmarket.models.UserViewModel
import com.yoloapps.backyardmarket.data.classes.Product
import com.yoloapps.backyardmarket.fragments.SellerHeaderFragment
import com.yoloapps.backyardmarket.fragments.SellingFragment
import com.yoloapps.backyardmarket.views.ProductView

class UserActivity : AppCompatActivity() {
    companion object {
        const val ARG_USER = "user"

        const val FOLLOWING_INDEX = 0
    }
    private lateinit var viewModel: UserViewModel
    lateinit var user: UserProfile
    lateinit var name: TextView
    lateinit var description: TextView
    lateinit var image: ImageView
    lateinit var follow: Button
    lateinit var contact: Button
    lateinit var new: FloatingActionButton
    lateinit var content: ViewGroup

    private lateinit var headerFrag: SellerHeaderFragment
    private lateinit var sellingFrag: SellingFragment

    private var following = false
    private var menu: Menu? = null

    private val repo by lazy { FirestoreRepository.getInstance(applicationContext) }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.user_activity_menu, menu)
        this.menu = menu

        val observer = Observer<DocumentSnapshot> {
            Log.d("XXXXX followingObs", "snap: $it.data")
            var icon = R.drawable.ic_bookmark_hollow
            if(it.data == null || it.data!!.isEmpty()) {

            }
            else {
                val data = it.data!!.values.toList()
                following = data[0] as Boolean
                Log.d("XXXXX followingObs", "following: $following")
                if (following) {
                    icon = R.drawable.ic_bookmark
                }
            }
            menu?.getItem(FOLLOWING_INDEX)?.icon = ContextCompat.getDrawable(this, icon)
        }
        viewModel.getFollowing(user.uid!!).observe(this, observer)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() //Call the back button's method
                return true
            }
            R.id.action_follow -> {
                if(following) {
                    Log.d("XXXXXXX", "following: "+user.displayName)
                    repo.removeFollowing(user)
                } else {
                    Log.d("XXXXXXX", "unfollowing: "+user.displayName)
                    repo.addFollowing(user)
                }
                return true
            }
            else -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = intent.getParcelableExtra(ARG_USER)
        setContentView(R.layout.activity_user)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        setSupportActionBar(findViewById(R.id.transparent_toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = user.displayName

        headerFrag = supportFragmentManager.findFragmentById(R.id.header) as SellerHeaderFragment
        headerFrag.userProfile = user

        sellingFrag = supportFragmentManager.findFragmentById(R.id.selling) as SellingFragment
        sellingFrag.user = user.uid
    }

    fun cancel (view: View) {
        finish()
    }

    fun contact (view: View) {

    }
}
