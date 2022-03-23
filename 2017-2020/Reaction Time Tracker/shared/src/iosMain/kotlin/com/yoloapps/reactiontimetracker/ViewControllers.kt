package com.yoloapps.reactiontimetracker

import com.yoloapps.reactiontimetracker.controllers.*
import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker

//@ThreadLocal
object ViewControllers {
    val mainController = MainController()
    val testController = TestController()
    val postTestController = PostTestController()

//    fun test(testController: TestController) {
//        Worker.start().execute(TransferMode.SAFE, {
//            testController
//        }) { input ->
//            input.onStart()
//            input
//        }
//    }
}