package com.yoloapps.reactiontimetracker

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.yoloapps.reactiontimetracker.controllers.ViewController

open class ControlledFragment<T: ViewController> : Fragment() {
    lateinit var controller: T

    init {
        controller = onInstantiateController()
//        controller = ViewController.newInstance()
    }

    open fun onInstantiateController(): T {
        return ViewController() as T
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        controller.onViewStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller.onViewVisible()
    }

    override fun onPause() {
        super.onPause()
        controller.onViewPaused()
    }

    override fun onResume() {
        super.onResume()
        controller.onViewResumed()
    }
}