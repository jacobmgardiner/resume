package com.yoloapps.reactiontimetracker

import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch

//expect object ConcurrenceUtils {
//    fun freeze(obj: Any)
//
////    fun testWorker(listener: TestEventListener, observer: DataObserver<Long>/*testController: TestController*/): TestController {
////        return Worker.start().execute(TransferMode.SAFE, {
////            println("[TEST WORKER] STARTING PRODUCER")
////            TestController().also {
////                it.addTestEventListener(listener)
////                it.observableTimeElapsed.addObserver(observer)
////                println("[TEST WORKER] INPUT: ${it.test}")
////                it.freeze()
////            }
////        }) { input ->
////            println("[TEST WORKER] STARTING JOB")
////            println("[TEST WORKER] INPUT: ${input.test}")
////            println("[TEST WORKER] LISTENERS: ${input.test.listeners}")
////            input.onStart()
////            input
////        }.result
////    }
//
//    fun<T> getAtomicReference(initial: T): T
//
//    fun dispatchOnMain(block: Runnable)
//
//    fun dispatchOnOther(block: Runnable)
//}