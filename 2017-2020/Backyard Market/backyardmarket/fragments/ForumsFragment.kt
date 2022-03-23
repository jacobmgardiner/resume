package com.yoloapps.backyardmarket.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.models.ForumsViewModel


class ForumsFragment : Fragment() {

    companion object {
        fun newInstance() =
            ForumsFragment()
    }

    private lateinit var viewModel: ForumsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.forums_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ForumsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
