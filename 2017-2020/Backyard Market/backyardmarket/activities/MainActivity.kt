package com.yoloapps.backyardmarket.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.yoloapps.backyardmarket.*
import com.yoloapps.backyardmarket.fragments.*


class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
    companion object {
        enum class PermissionRequest {
            LOCATION
        }
    }

    /**
     * A list of icons for the navigation tabs.
     */
    private var icons = arrayOf(
        R.layout.icon_tab_home,
        R.layout.icon_tab_shop,
        R.layout.icon_tab_map,
        R.layout.icons_tab_connect
//        R.layout.icon_tab_shop
    )

    private val tabs by lazy { findViewById<TabLayout>(R.id.tabLayout) }

    private val sellerDashboardFragment by lazy { SellerDashboardFragment.newInstance() }
    private val shopFragment by lazy { ShopFragment.newInstance() }
    private val mapShopFragment by lazy { MapShopFragment.newInstance() }
    private val connectFragment by lazy { ConnectFragment.newInstance() }

    private val auth by lazy { FirebaseAuth.getInstance() }

    private fun loginStatus(): BackyardApplication.Companion.LoginStatus {
        val user = auth.currentUser
        return when {
            user != null -> { BackyardApplication.Companion.LoginStatus.LOGGED_IN }
            user?.isEmailVerified == false -> { BackyardApplication.Companion.LoginStatus.NOT_VERIFIED }
            else -> { BackyardApplication.Companion.LoginStatus.LOGGED_OUT }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionRequest.LOCATION.ordinal -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    mapShopFragment.onPermissionGranted()
                } else {
                    mapShopFragment.onPermissionDenied()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        when(loginStatus()) {
            BackyardApplication.Companion.LoginStatus.LOGGED_IN -> {  }
            BackyardApplication.Companion.LoginStatus.LOGGED_OUT -> { startActivity(Intent(this, StartActivity::class.java)) }
            BackyardApplication.Companion.LoginStatus.NOT_VERIFIED -> { startActivity(Intent(this, VerifyActivity::class.java)) }
            else -> {  }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO("remove boilerplate??")
        setSupportActionBar(findViewById(R.id.my_toolbar))


        // setup the tabs
        val vp: ViewPager = findViewById(R.id.viewPager)
        vp.adapter =
            MainPagerAdapter(
                supportFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                listOf(
                    sellerDashboardFragment,
                    shopFragment,
                    mapShopFragment,
                    connectFragment
                )
            )
        tabs.setupWithViewPager(vp)
        for (i in 0 until tabs.tabCount) {
            tabs.getTabAt(i)!!.setCustomView(icons[i])
        }

        tabs.addOnTabSelectedListener(this)
    }

    /**
     * The PagerAdapter for the main activity's navigation tabs.
     */
    private class MainPagerAdapter(fm: FragmentManager, behavior: Int, private val fragments: List<Fragment>) :
        FragmentPagerAdapter(fm, behavior) {

        override fun getItem(position: Int): Fragment {
            // return the appropriate fragment
//            return when (position) {
//                0 -> fragments[0]
//                1 -> fragments[1]
//                2 -> fragments[2]
//                else -> {
//                    return ConnectFragment()
//                }
//            }
            return fragments[position]
        }

        override fun getCount(): Int {
            return 4
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
//        TODO("Not yet implemented")
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        // add a gold tint to the selected tab
        val tabIconColor = ContextCompat.getColor(this,
            R.color.colorAccent
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            tab?.icon?.colorFilter = BlendModeColorFilter(tabIconColor, BlendMode.SRC_IN)
        } else {
            tab?.icon?.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        tab?.icon?.clearColorFilter() //clear the gold tint
    }

    override fun onDestroy() {
        super.onDestroy()
        tabs.removeOnTabSelectedListener(this)
    }
}
