package com.yoloapps.backyardmarket.activities

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.yoloapps.backyardmarket.*
import com.yoloapps.backyardmarket.data.FirestoreRepository
import com.yoloapps.backyardmarket.data.classes.Offer
import com.yoloapps.backyardmarket.data.classes.Product
import com.yoloapps.backyardmarket.data.classes.UserProfile
import com.yoloapps.backyardmarket.fragments.*
import com.yoloapps.backyardmarket.models.ProductViewModel
import com.yoloapps.backyardmarket.utils.TimestampUtils


class ProductActivity : AppCompatActivity() {
    companion object {
        const val ARG_PRODUCT = "product"

        const val BOOKMARK_INDEX = 0
    }

    var product: Product? = null
        set(value) {
            field = value
        }

    var offers: List<Offer>? = null

    private lateinit var viewModel: ProductViewModel

    lateinit var buttons: LinearLayout
    lateinit var contact: Button
    lateinit var buy: Button

    lateinit var container: LinearLayout

    private lateinit var header: ProductHeaderFragment
    private lateinit var seller: SellerFragment
    private lateinit var price: PriceFragment
    private lateinit var offersFrag: OffersFragment
    private lateinit var location: LocationFragment

    private var buyerIndex = -2

    private var bookmarked = false

    private var sellerProfile: UserProfile? = null

    private var menu: Menu? = null

    private val repo by lazy { FirestoreRepository.getInstance(applicationContext) }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.product_activity_menu, menu)
        this.menu = menu

        val bookmarkedObserver = Observer<DocumentSnapshot> {
            var icon = R.drawable.ic_bookmark_hollow
            if(it.data == null || it.data!!.isEmpty()) {

            }
            else {
                bookmarked = it.data?.get(FirestoreRepository.FIELD_IS_BOOKMARKED) as Boolean
                if (bookmarked) {
                    icon = R.drawable.ic_bookmark
                }
            }
            menu?.getItem(BOOKMARK_INDEX)?.icon = ContextCompat.getDrawable(this, icon)
        }
        viewModel.getBookmarked(product?.productId!!).observe(this, bookmarkedObserver)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed() //Call the back button's method
                return true
            }
            R.id.action_bookmark -> {
                if(bookmarked) {
                    unbookmark()
                } else {
                    bookmark()
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

        product = intent.getParcelableExtra(ARG_PRODUCT)

        viewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        val offersObserved = Observer<QuerySnapshot> {
            offers = repo.docsToObject(it, Offer::class.java)
            Log.d("XXXXXXXXXXXX", "OFFERS: $offers")
            buyerIndex = isBuyer()
            when (buyerIndex) {
                -2 -> {
                    offersFrag = OffersFragment.newInstance(offers!!)
                    supportFragmentManager.beginTransaction()
                        .replace(container.id, offersFrag, "fragment:offers")
                        .commit()
                    Log.d("XXXXXXXXXXXX", "seller")
                }
                -1 -> {
                    Log.d("XXXXXXXXXXXX", "not a buyer")
                }
                else -> {
                    offersFrag = OffersFragment.newInstance(offers!![buyerIndex])
                    supportFragmentManager.beginTransaction()
                        .replace(container.id, offersFrag, "fragment:offers")
                        .commit()
                    Log.d("XXXXXXXXXXXX", "buyer: $buyerIndex")
                }
            }
        }
        viewModel.getOffers(product?.productId!!).observe(this, offersObserved)

        setContentView(R.layout.activity_product)

        setSupportActionBar(findViewById(R.id.transparent_toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = product!!.title

        container = findViewById(R.id.frags_container)

        header = supportFragmentManager.findFragmentById(R.id.header) as ProductHeaderFragment
        header.editable = (repo.uid == product!!.sellerUid)
        header.product = product

        buttons = findViewById(R.id.buttons)
        buy = findViewById(R.id.offer)
        contact = findViewById(R.id.contact)

        buyerIndex = isBuyer()
        when(buyerIndex) {
            -2 -> {
                initSell()
            }
            else -> {
                initBuy()
            }
        }
    }

    private fun initSell() {
        header.editable = true

        buy.text = "Edit"
        buy.setOnClickListener {
            val intent = Intent(this, SellActivity::class.java)
            intent.putExtra(SellActivity.ARG_PRODUCT, product)
            startActivity(intent)
        }

        buttons.visibility = View.GONE
    }

    private fun isBuyer(): Int {
        if(repo.uid!! == product!!.sellerUid) {
            return -2
        }
        if(offers != null) {
            for ((i, offer) in offers!!.withIndex()) {
                Log.d("XXXXXXXXXXX", "buyer? " + offer.buyerUid+", "+FirebaseAuth.getInstance().currentUser!!.uid)
                if (offer.buyerUid == FirebaseAuth.getInstance().currentUser!!.uid)
                    return i
            }
        }
        return -1
    }

    private fun initBuy() {
        product!!.sellerUid?.let {
            repo.getUserProfileCache(it)
                .addOnSuccessListener { profile ->
                    sellerProfile = profile.toObject(UserProfile::class.java)

                    seller = SellerFragment.newInstance(sellerProfile!!)
                    supportFragmentManager.beginTransaction()
                        .add(container.id, seller, "fragment:seller")
                        .commit()

                    location = LocationFragment.newInstance(product!!)
                    supportFragmentManager.beginTransaction()
                        .add(container.id, location, "fragment:location")
                        .commit()

                    //TODO rating

                    val buyer = isBuyer()
                    if(buyer > -1) {
    //                    offersFrag = OffersFragment.newInstance(offers[buyer])
    //                    supportFragmentManager.beginTransaction()
    //                        .add(container.id, offersFrag, "fragment:offers")
    //                        .commit()

                        buttons.visibility = View.GONE
                    } else {
                        price = PriceFragment.newInstance(product!!)
                        supportFragmentManager.beginTransaction()
                            .add(container.id, price, "fragment:price")
                            .commit()
                    }
            }
        }
    }

    fun contact (view: View) {
        //TODO go to message activity
        finish()
    }

    private fun openConversation(offer: Offer) {
        val intent = Intent(this, ConversationActivity::class.java)
        intent.putExtra(ConversationActivity.ARG_PRODUCT, product as Parcelable)
        intent.putExtra(ConversationActivity.ARG_OFFER, offer)
        intent.putExtra(ConversationActivity.ARG_IS_NEW, false)
        startActivity(intent)
    }

    private fun makeOffer() {
        val offer = Offer(
            productId = product!!.productId,
            buyerUid = repo.uid,
            sellerUid = product!!.sellerUid,
            amount = price.selectedAmount,
            unit = price.selectedUnit,
            price = price.price,
            time = TimestampUtils.timestamp
        )

        //open conversation
        val intent = Intent(this, ConversationActivity::class.java)
        intent.putExtra(ConversationActivity.ARG_PRODUCT, product as Parcelable)
        intent.putExtra(ConversationActivity.ARG_OFFER, offer)
        intent.putExtra(ConversationActivity.ARG_IS_NEW, true)
        startActivity(intent)
    }

    fun buy(view: View) {
        val buyer = isBuyer()
        if(buyer > -1) {
            offers?.get(buyer)?.let { openConversation(it) }
        } else {
            makeOffer()
        }
    }

    private fun bookmark() {
        repo.addBookmark(product!!)
    }

    private fun unbookmark() {
        repo.removeBookmark(product?.productId!!)
    }
}
