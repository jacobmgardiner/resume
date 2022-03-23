package com.yoloapps.reactiontimetracker.controllers

import com.yoloapps.reactiontimetracker.*
import com.yoloapps.reactiontimetracker.data.ObservableData
import kotlinx.coroutines.*
import kotlin.native.concurrent.ThreadLocal

//@ThreadLocal
//val test = Test()
//val testAR = AtomicReferenceKMM(Test())

//@ThreadLocal
class TestController : ViewController() {
    val app by lazy { Application }

    val testAR = AtomicReferenceKMM(Test())

//    lateinit var observableTimeElapsed: ObservableData<Long>
//    val test by lazy { Test() }
    lateinit var observableTimeElapsed: ObservableData<Long>

    suspend fun onStart(/*eventListener: TestEventListener*/)/* = withContext(Dispatchers.Default)*/ {
//        val test = Test(eventListener)
//        DispatcherUtils.dispatchOnOther {
        GlobalScope.launch {
            println("[TEST CONTROLLER] OBSERVING ELAPSED TIME")
            observableTimeElapsed = testAR.value.elapsedTime
            println("[TEST CONTROLLER] STARTING TEST FROM CONTROLLER")
            testAR.value.start()
            println("[TEST CONTROLLER] STARTED TEST FROM CONTROLLER")
        }
    }

    fun addTestEventListener(listener: TestEventListener) /*= withContext(Dispatchers.Default)*/ {
//        DispatcherUtils.dispatchOnOther {
            testAR.value.addEventListener(listener)
//        }
    }

    fun onTap() /*= withContext(Dispatchers.Default)*/ {
//        DispatcherUtils.dispatchOnOther {
            testAR.value.onTap()
//        }
    }

    fun onTestEnd() /*= withContext(Dispatchers.Default)*/ {
//        DispatcherUtils.dispatchOnOther {
            app.repo.storeTest(testAR.value.data)
//        }
    }

    fun onPrompt() {
        //TODO
    }
}