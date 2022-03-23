package com.yoloapps.backyardmarket.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.classes.Suggestion


class SuggestionView (context: Context) : CardView(context) {
    private lateinit var title: TextView
    private lateinit var content: TextView
    private lateinit var learnMore: TextView
    private lateinit var dismiss: TextView

    private lateinit var suggestion: Suggestion

    private var loadedPic = false
    private var noListen = false

    constructor(context: Context, suggestion: Suggestion) : this(context) {
        update(suggestion)
    }

    fun update(suggestion: Suggestion) {
        this.suggestion = suggestion
        inflate(context,
            R.layout.suggestion_view, this)

        title = findViewById(R.id.title)
        title.text = suggestion.title

        content = findViewById(R.id.content)
        content.text = suggestion.content

        learnMore = findViewById(R.id.learn_more)

        dismiss = findViewById(R.id.dismiss)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        learnMore.setOnClickListener {
            val webpage: Uri = Uri.parse(suggestion.link)
            val intent = Intent(Intent.ACTION_VIEW, webpage)
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }

        }

        dismiss.setOnClickListener {
            //TODO
            visibility = View.GONE
        }
    }
}