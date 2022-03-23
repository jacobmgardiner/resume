package com.yoloapps.backyardmarket.data

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult
import com.google.protobuf.StringValue
import com.yoloapps.backyardmarket.data.classes.*
import kotlin.random.Random

/**
 *
 */
class FirestoreRepository(val context: Context) {
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: FirestoreRepository? = null
        fun getInstance(context: Context): FirestoreRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: FirestoreRepository(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }

        enum class Collection(val stringValue: String) {
            COLLECTION_BUYING("buying"),
            COLLECTION_BOOKMARKS("bookmarks"),
            COLLECTION_FOLLOWING("following"),
            COLLECTION_SUGGESTIONS("suggestions"),
            COLLECTION_OFFERS("offers"),
            COLLECTION_PRODUCTS("products"),
            COLLECTION_PRODUCT_DATA("productData"),
            COLLECTION_USERS("users"),
            COLLECTION_USER_TOKENS("notificationTokens"),
            COLLECTION_FOLLOWERS("followers"),
            COLLECTION_CONVERSATIONS("conversations"),
            COLLECTION_STRIPE_DATA("stripeData"),
            COLLECTION_ACCEPTED("acceptedOffers")
        }

        const val DOCUMENT_PRIVATE: String = "private"

        const val FIELD_TOKEN = "token"

        const val FIELD_IS_BUYING = "buying"
        const val FIELD_IS_SELLING = "selling"
        const val FIELD_IS_BOOKMARKED = "bookmarked"
        const val FIELD_IS_FOLLOWING = "follow"
        const val FIELD_IS_FOLLOWER = "follow"
        const val FIELD_ACCEPTED = "accepted"

        const val FIELD_STATE = "state"
        const val FIELD_ACCOUNT = "account"

        const val FUNCTION_CREATE_STRIPE_ACCOUNT = "createStripeAccount"
        const val FUNCTION_CREATE_PAYMENT_INTENT = "createPaymentIntent"

        const val MAX_SUGGESTION_INDEX = 1

        const val TAG_FIRESTORE_CALL = "FirestoreCall"
    }

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val functions by lazy { FirebaseFunctions.getInstance() }

    //users
    //  {uid}
    //      stripeData
    //          secret (accountId)
    //          private (state)
    //          public

    val uid: String?
        get() {
            return if (auth.currentUser != null)
                auth.currentUser?.uid
            else
                null
        }

    val currentUser: FirebaseUser?
        get() {
            return if (auth.currentUser != null)
                auth.currentUser
            else
                null
        }

    private var callCount = 0
    fun callCount() {
        callCount++
        Log.d(TAG_FIRESTORE_CALL, "count: $callCount")
    }

    fun getUserProfile(): FirebaseDocumentLiveData {
        return FirebaseDocumentLiveData(
            context.applicationContext,
            db.collection(Collection.COLLECTION_USERS.stringValue).document(uid!!)
        )
    }

    fun updateUserProfile(userProfile: UserProfile) {

    }

    fun getUserProfileLive(uid: String): FirebaseDocumentLiveData {
        return FirebaseDocumentLiveData(
            context.applicationContext,
            db.collection(Collection.COLLECTION_USERS.stringValue).document(uid)
        )
    }

    fun getUserProfileCache(uid: String): Task<DocumentSnapshot> {
//        var task: Task<DocumentSnapshot> = db.collection(COLLECTION_USERS).document(uid).get(Source.CACHE)
//        Tasks.await(
//            task.continueWith {
//                if (!it.isSuccessful)
//                    task = getUserProfileServer(uid)
//            }
//        )
//        return task
        return db.collection(Collection.COLLECTION_USERS.stringValue).document(uid).get()
    }

    fun getUserProfileServer(uid: String): Task<DocumentSnapshot> {
        return db.collection(Collection.COLLECTION_USERS.stringValue).document(uid).get(Source.SERVER)
    }

    fun getSuggestion(): FirebaseQueryLiveData {
        val randomIndex = kotlin.random.Random.nextInt(0, MAX_SUGGESTION_INDEX)
        val suggestions = FirebaseFirestore.getInstance().collection(Collection.COLLECTION_SUGGESTIONS.stringValue)
        return FirebaseQueryLiveData(
            suggestions
            .whereEqualTo(Suggestion.INDEX, randomIndex)
        )
    }

    fun getFollowersUids(): FirebaseQueryLiveData {
        return FirebaseQueryLiveData(
            db.collection(Collection.COLLECTION_USERS.stringValue)
                .document(uid!!)
                .collection(Collection.COLLECTION_FOLLOWERS.stringValue)
        )
    }

    fun getIsFollower(user: String): FirebaseDocumentLiveData {
        return FirebaseDocumentLiveData(
            context.applicationContext,
            db.collection(Collection.COLLECTION_USERS.stringValue)
                .document(uid!!)
                .collection(Collection.COLLECTION_FOLLOWERS.stringValue)
                .document(user)
        )
    }

    fun getBuyingUids(): FirebaseQueryLiveData {
        return FirebaseQueryLiveData(
            db.collection(Collection.COLLECTION_USERS.stringValue)
                .document(uid!!)
                .collection(Collection.COLLECTION_BUYING.stringValue)
        )
    }

    fun addBuying(product: Product) {
        db.collection(Collection.COLLECTION_USERS.stringValue)
            .document(uid!!)
            .collection(Collection.COLLECTION_BUYING.stringValue)
            .document(product.productId!!)
            .set(
                mapOf(
                    FIELD_IS_BUYING to true
                )
            )
    }

    fun getSelling(): FirebaseQueryLiveData {
        return FirebaseQueryLiveData(
            db.collection(Collection.COLLECTION_PRODUCTS.stringValue)
                .whereEqualTo(Product.SELLER_UID, uid)
        )
    }

    fun getSelling(id: String): FirebaseQueryLiveData {
        return FirebaseQueryLiveData(
            db.collection(Collection.COLLECTION_PRODUCTS.stringValue)
                .whereEqualTo(Product.SELLER_UID, id)
        )
    }

    fun getBookmarksIds(): FirebaseQueryLiveData {
        return FirebaseQueryLiveData(
            db.collection(Collection.COLLECTION_USERS.stringValue)
                .document(uid!!)
                .collection(Collection.COLLECTION_BOOKMARKS.stringValue)
        )
    }

    fun getBookmarkedIds(): FirebaseQueryLiveData {
        return FirebaseQueryLiveData(
            db.collection(Collection.COLLECTION_USERS.stringValue)
                .document(uid!!)
                .collection(Collection.COLLECTION_BOOKMARKS.stringValue)
                .whereEqualTo(FIELD_IS_BOOKMARKED, true)
        )
    }

    fun isBookmarkedLive(productId: String): FirebaseDocumentLiveData {
        return FirebaseDocumentLiveData(
            context.applicationContext,
            db.collection(Collection.COLLECTION_USERS.stringValue)
                .document(uid!!)
                .collection(Collection.COLLECTION_BOOKMARKS.stringValue)
                .document(productId)
        )
    }

    fun isBookmarked(productId: String, result: (Boolean) -> Unit): Task<DocumentSnapshot> {
        return db.collection(Collection.COLLECTION_USERS.stringValue)
            .document(uid!!)
            .collection(Collection.COLLECTION_BOOKMARKS.stringValue)
            .document(productId)
            .get()
            .addOnSuccessListener {
                result(it.toObject(Boolean::class.java)!!)
            }
    }

    fun addBookmark(product: Product): Task<Void> {
        return db.collection(Collection.COLLECTION_USERS.stringValue)
            .document(uid!!)
            .collection(Collection.COLLECTION_BOOKMARKS.stringValue)
            .document(product.productId!!)
            .set(
                mapOf(
                    FIELD_IS_BOOKMARKED to true
                )
            )
    }

    fun removeBookmark(productId: String): Task<Void> {
        return db.collection(Collection.COLLECTION_USERS.stringValue)
            .document(uid!!)
            .collection(Collection.COLLECTION_BOOKMARKS.stringValue)
            .document(productId)
            .set(mapOf(FIELD_IS_BOOKMARKED to false))
//            .delete()
    }

    fun getOffers(productId: String): FirebaseQueryLiveData {
        return FirebaseQueryLiveData(
            db.collection(Collection.COLLECTION_PRODUCTS.stringValue)
                .document(productId)
                .collection(Collection.COLLECTION_OFFERS.stringValue)
        )
    }

    fun getOffer(productId: String, buyerUid: String): Task<DocumentSnapshot> {
            return db.collection(Collection.COLLECTION_PRODUCTS.stringValue)
                .document(productId)
                .collection(Collection.COLLECTION_OFFERS.stringValue)
                .document(buyerUid)
                .get()
    }

    fun addOffer(offer: Offer) {
            db.collection(Collection.COLLECTION_PRODUCTS.stringValue)
                .document(offer.productId!!)
                .collection(Collection.COLLECTION_OFFERS.stringValue)
                .document(offer.buyerUid!!)
                .set(offer)
    }

    fun getFollowing(): FirebaseQueryLiveData {
        return FirebaseQueryLiveData(
            db.collection(Collection.COLLECTION_USERS.stringValue)
                .document(uid!!)
                .collection(Collection.COLLECTION_FOLLOWING.stringValue)
        )
    }

    fun getFollowing(following: String): FirebaseDocumentLiveData {
        return FirebaseDocumentLiveData(
            context.applicationContext,
            db.collection(Collection.COLLECTION_USERS.stringValue)
                .document(uid!!)
                .collection(Collection.COLLECTION_FOLLOWING.stringValue)
                .document(following)
        )
    }

    private fun addFollower(userProfile: UserProfile) {
        getUserProfileCache(uid!!)
            .addOnSuccessListener {
                db.collection(Collection.COLLECTION_USERS.stringValue)
                    .document(userProfile.uid!!)
                    .collection(Collection.COLLECTION_FOLLOWERS.stringValue)
                    .document(uid!!)
                    .set(
                        mapOf(
                            FIELD_IS_FOLLOWER to true
                        )
                    )
            }
    }

    private fun removeFollower(userProfile: UserProfile): Task<DocumentSnapshot> {
        return getUserProfileCache(uid!!)
            .addOnSuccessListener {
                db.collection(Collection.COLLECTION_USERS.stringValue)
                    .document(userProfile.uid!!)
                    .collection(Collection.COLLECTION_FOLLOWERS.stringValue)
                    .document(uid!!)
                    .set(
                        mapOf(
                            FIELD_IS_FOLLOWER to false
                        )
                    )
            }
    }

    fun addFollowing(userProfile: UserProfile) {
        db.collection(Collection.COLLECTION_USERS.stringValue)
            .document(uid!!)
            .collection(Collection.COLLECTION_FOLLOWING.stringValue)
            .document(userProfile.uid!!)
            .set(
                mapOf(
                    FIELD_IS_FOLLOWING to true
                )
            )

        addFollower(userProfile)
    }

    fun removeFollowing(userProfile: UserProfile): Task<DocumentSnapshot> {
        db.collection(Collection.COLLECTION_USERS.stringValue)
            .document(uid!!)
            .collection(Collection.COLLECTION_FOLLOWING.stringValue)
            .document(userProfile.uid!!)
            .set(
                mapOf(
                    FIELD_IS_FOLLOWING to false
                )
            )
        return removeFollower(userProfile)
    }

    fun getFeatured(): FirebaseQueryLiveData {
        return FirebaseQueryLiveData(
            db.collection(Collection.COLLECTION_USERS.stringValue)
                .whereEqualTo(UserProfile.FEATURED, true)
        )
    }

    fun getProduct(id: String): FirebaseDocumentLiveData {
        return FirebaseDocumentLiveData(
            context.applicationContext,
            db.collection(Collection.COLLECTION_PRODUCTS.stringValue).document(id)
        )
    }

    fun getProductCache(productId: String): Task<DocumentSnapshot> {
//        var task: Task<DocumentSnapshot> = db.collection(Collection.COLLECTION_PRODUCTS.stringValue).document(productId).get(Source.CACHE)
//        Tasks.await(
//            task.continueWith {
//                if (!it.isSuccessful)
//                    task = getProductServer(productId)
//            }
//        )
//        return task
        return db.collection(Collection.COLLECTION_PRODUCTS.stringValue).document(productId).get()
    }

    fun getProductServer(productId: String): Task<DocumentSnapshot> {
        return db.collection(Collection.COLLECTION_PRODUCTS.stringValue).document(productId).get(Source.SERVER)
    }

    fun getNewProductId(): String {
        return db.collection(Collection.COLLECTION_PRODUCTS.stringValue).document().id
    }

    fun uploadProduct(product: Product): Task<Void> {
        return db.collection(Collection.COLLECTION_PRODUCTS.stringValue).document(product.productId!!).set(product)
            .addOnCompleteListener {
                if (it.exception != null) {
                    Log.e("XXXXXXXx uploadProduct", it.exception!!.message)
                }
            }
    }

    fun getTypes(category: Int): Task<QuerySnapshot> {
        return db.collection(Collection.COLLECTION_PRODUCT_DATA.stringValue)
            .whereEqualTo(Type.CATEGORY, category)
            .get(/*Source.CACHE*/)
    }

    fun getConversation(productId: String, buyerUid: String): FirebaseDocumentLiveData {
        return FirebaseDocumentLiveData(
            context.applicationContext,
            db.collection(Collection.COLLECTION_PRODUCTS.stringValue)
                .document(productId)
                .collection(Collection.COLLECTION_OFFERS.stringValue)
                .document(buyerUid)
                .collection(Collection.COLLECTION_CONVERSATIONS.stringValue)
                .document(buyerUid)
        )
    }

    fun startConversation(offer: Offer, conversation: Conversation): Task<Void> {
        return db.collection(Collection.COLLECTION_PRODUCTS.stringValue)
            .document(offer.productId!!)
            .collection(Collection.COLLECTION_OFFERS.stringValue)
            .document(offer.buyerUid!!)
            .collection(Collection.COLLECTION_CONVERSATIONS.stringValue)
            .document(offer.buyerUid!!)
            .set(conversation)
    }

    fun getConversation(offer: Offer): FirebaseDocumentLiveData {
        return FirebaseDocumentLiveData(
            context.applicationContext,
            db.collection(Collection.COLLECTION_PRODUCTS.stringValue)
                .document(offer.productId!!)
                .collection(Collection.COLLECTION_OFFERS.stringValue)
                .document(offer.buyerUid!!)
                .collection(Collection.COLLECTION_CONVERSATIONS.stringValue)
                .document(offer.buyerUid!!)
        )
    }

    fun sendMessage(productId: String, buyerUid: String, message: Message) {
        db.collection(Collection.COLLECTION_PRODUCTS.stringValue)
            .document(productId)
            .collection(Collection.COLLECTION_CONVERSATIONS.stringValue)
            .document(buyerUid)
            .update(Conversation.MESSAGES, FieldValue.arrayUnion(message))
    }

    fun sendMessage(offer: Offer, message: Message) {
        db.collection(Collection.COLLECTION_PRODUCTS.stringValue)
            .document(offer.productId!!)
            .collection(Collection.COLLECTION_OFFERS.stringValue)
            .document(offer.buyerUid!!)
            .collection(Collection.COLLECTION_CONVERSATIONS.stringValue)
            .document(offer.buyerUid)
            .update(Conversation.MESSAGES, FieldValue.arrayUnion(message))
    }

    fun getNotificationTokens(): Task<QuerySnapshot> {
        return db.collection(Collection.COLLECTION_USERS.stringValue)
            .document(auth.currentUser?.uid ?: "")
            .collection(Collection.COLLECTION_USER_TOKENS.stringValue)
            .get()
    }

    fun uploadNotificationToken(token: String) {
        if (uid == null) return
        db.collection(Collection.COLLECTION_USERS.stringValue)
            .document(auth.currentUser?.uid!!)
            .collection(Collection.COLLECTION_USER_TOKENS.stringValue)
            .document(token)
            .set(mapOf(FIELD_TOKEN to token))
    }

    fun <T : Any?> docsToObject(snapshot: QuerySnapshot, type: Class<T>): List<T> {
        return List(snapshot.documents.size) { i ->
            snapshot.documents[i].toObject(type)!!
        }
    }

    fun <T> getObjectCache(id: String, type: Class<T>): Task<DocumentSnapshot> {
        return when (type) {
            UserProfile::class.java -> {
                db.collection(Collection.COLLECTION_USERS.stringValue).document(id).get()
            }
            Product::class.java -> {
                db.collection(Collection.COLLECTION_PRODUCTS.stringValue).document(id).get()
            }
            else -> throw IllegalArgumentException()
        }
    }

    fun uploadPaymentToken() {

    }

    fun requestPaymentIntent(productId: String, success: (String) -> Unit) {
        functions.getHttpsCallable(FUNCTION_CREATE_PAYMENT_INTENT).call(hashMapOf("productId" to productId))
            .addOnSuccessListener {
                success(it.data.toString())
            }
            .addOnFailureListener {
                Log.e("MMMMMMM", it.message)
            }
    }

    fun getRandomRedirectState(): String {
        val state = generateRandomState(10)
        uploadRedirectState(state)
        return state
    }

    fun generateRandomState(length: Int): String {
        var state = "sv_"
        for (i in 0 until length)
            state += Random.nextInt(0, 9)
        return state
    }

    private fun uploadRedirectState(state: String): Task<Void> {
        return db.collection(Collection.COLLECTION_USERS.stringValue)
            .document(uid!!)
            .collection(Collection.COLLECTION_STRIPE_DATA.stringValue)
            .document(DOCUMENT_PRIVATE)
            .set(hashMapOf("state" to state), SetOptions.merge())
    }

    fun getSavedRedirectState(success: (String?) -> Unit): Task<DocumentSnapshot> {
        return db.collection(Collection.COLLECTION_USERS.stringValue)
            .document(uid!!)
            .collection(Collection.COLLECTION_STRIPE_DATA.stringValue)
            .document(DOCUMENT_PRIVATE)
            .get()
            .addOnSuccessListener {
                success(it.toObject(PrivateStripeData::class.java)?.state)
            }
    }

    fun createStripeAccount(code: String?): Task<HttpsCallableResult> {
        return functions.getHttpsCallable(FUNCTION_CREATE_STRIPE_ACCOUNT).call(hashMapOf("code" to code))
    }

    fun getAcceptedOfferBuyerId(offer: Offer): FirebaseDocumentLiveData {
        return FirebaseDocumentLiveData(
            context.applicationContext,
            db.collection(Collection.COLLECTION_PRODUCTS.stringValue)
                .document(offer.productId!!)
                .collection(Collection.COLLECTION_ACCEPTED.stringValue)
                .document(offer.productId)
        )
    }

    fun accept(offer: Offer): Task<Void> {
        return db.collection(Collection.COLLECTION_PRODUCTS.stringValue)
            .document(offer.productId!!)
            .collection(Collection.COLLECTION_ACCEPTED.stringValue)
            .document(offer.productId)
            .set(
                mapOf(
                    FIELD_ACCEPTED to offer.buyerUid
                )
            )
    }

    fun unaccept(offer: Offer): Task<Void> {
        return db.collection(Collection.COLLECTION_PRODUCTS.stringValue)
            .document(offer.productId!!)
            .collection(Collection.COLLECTION_ACCEPTED.stringValue)
            .document(offer.productId)
            .set(
                mapOf(
                    FIELD_ACCEPTED to null
                )
            )
    }

    fun getIsConnectedToStripe(success: (Boolean) -> Unit): Task<DocumentSnapshot> {
        return db.collection(Collection.COLLECTION_USERS.stringValue)
            .document(uid!!)
            .collection(Collection.COLLECTION_STRIPE_DATA.stringValue)
            .document(DOCUMENT_PRIVATE)
            .get()
            .addOnSuccessListener {
                val priv = it.toObject(PrivateStripeData::class.java)
                Log.d("XXXXXXXX", priv.toString())
                success(priv?.account ?: false)
            }
            .addOnFailureListener { Log.e("XXXXXX", "error checking if connected to stripe: ${it.message}") }
    }

    fun getPrivateStripeDataLive(): FirebaseDocumentLiveData {
        return FirebaseDocumentLiveData(
            context.applicationContext,
            db.collection(Collection.COLLECTION_USERS.stringValue)
                .document(uid!!)
                .collection(Collection.COLLECTION_STRIPE_DATA.stringValue)
                .document(DOCUMENT_PRIVATE)
        )
    }

//    fun setIsConnectedToStripe(connected: Boolean): Task<Void> {
//        return db.collection(Collection.COLLECTION_USERS.stringValue)
//            .document(uid!!)
//            .collection(Collection.COLLECTION_STRIPE_DATA.stringValue)
//            .document(DOCUMENT_PRIVATE)
//            .update(FIELD_ACCOUNT, connected)
//    }
}