package com.yoloapps.backyardmarket.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.classes.Conversation
import com.yoloapps.backyardmarket.data.classes.Message
import com.yoloapps.backyardmarket.data.classes.Offer
import com.yoloapps.backyardmarket.models.MessagesViewModel
import com.yoloapps.backyardmarket.views.MessageView


class MessagesFragment : Fragment() {

    private lateinit var viewModel: MessagesViewModel
    private lateinit var recyclerView: RecyclerView

    private var firstLoad = true

    /**
     * The id for the conversation to load. The id is the product id followed by the offer number (the index of the offer in the product's offers array).
     */
    var offer: Offer? = null
        set(value) {
            field = value
            Log.d("XXXXXXX", "MessagesFrag:offer: "+value)
            if (value != null) {
                setupObserver()
            }
        }

    var messages: List<Message>? = null

    var isSeller: Boolean = false
        set(value) {
            field = value
            initRecycler()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            offer = it.getParcelable(ARG_OFFER)
            Log.d("XXXXXXX", "MessagesFrag:onCreate:offer: "+offer)
        }
    }

    private fun scrollToBottom() {
//        recyclerView.postDelayed({
        if (recyclerView.adapter != null)
            recyclerView.smoothScrollToPosition(
                recyclerView.adapter!!.itemCount.minus(1)
            )
//        }, 100)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.messages_fragment, container, false)

        recyclerView = view.findViewById(R.id.recycler)
        recyclerView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                scrollToBottom()
            }
        }
        initRecycler()

        return view
    }

    private fun initRecycler() {
        if (messages != null) {
            val isSeller = false
            val lm = LinearLayoutManager(requireContext())
//            lm.reverseLayout = true
            lm.stackFromEnd = true
            recyclerView.layoutManager = lm
            recyclerView.adapter = MessagesListAdapter(isSeller)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(MessagesViewModel::class.java)
        setupObserver()
    }

    private fun setupObserver() {
        if (this::viewModel.isInitialized && offer != null) {
            val observer = Observer<DocumentSnapshot> { conversation ->
                this.messages = conversation.toObject(Conversation::class.java)?.messages
                Log.d("XXXXXXXXX", "CONVERSATION: ${conversation.toObject(Conversation::class.java)}")
                if (this.messages != null)
                    for (mes in this.messages!!)Log.d("XXXXXXXXX", "MESSAGES: ${mes.content}")
                recyclerView.adapter?.notifyDataSetChanged()
                if(firstLoad)
                    initRecycler()
                else
                    scrollToBottom()
                firstLoad = false
            }
            viewModel.getMessages(offer!!).observe(viewLifecycleOwner, observer)
        }
    }

    companion object {

        const val ARG_OFFER = "conversationId"

        @JvmStatic
        fun newInstance(offer: Offer) =
            MessagesFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_OFFER, offer)
                }
            }
    }

    private inner class MessagesListAdapter(val isSeller: Boolean) : RecyclerView.Adapter<MessagesListAdapter.ViewHolder>() {
        /**
         * Provide a reference to the type of views that you are using (custom ViewHolder)
         */
        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            var messageView: MessageView = v as MessageView
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            // Create a new view.
            val messageView = LayoutInflater.from(parent.context).inflate(R.layout.message_view, MessageView(parent.context)) as MessageView

            messageView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            return ViewHolder(messageView)
        }

        override fun getItemCount(): Int {
            return messages!!.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            // Get element from your dataset at this position and replace the contents of the view
            // with that element
            holder.messageView.isSeller = isSeller
            holder.messageView.message = messages!![position]
//            Log.d("XXXXXXXXX", ""+position + ": "+messages[position].content)
        }

    }
}