package com.yoloapps.reactiontimetracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.yoloapps.reactiontimetracker.controllers.ViewController

open class ControlledActivity<T: ViewController> : FragmentActivity() {
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

    override fun onContentChanged() {
        super.onContentChanged()
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