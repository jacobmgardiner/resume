//package com.yoloapps.reactiontimetracker
//
//import com.yoloapps.reactiontimetracker.controllers.TestController
//import com.yoloapps.reactiontimetracker.data.DataObserver
//import kotlinx.atomicfu.AtomicRef
//import kotlinx.atomicfu.atomic
//import kotlinx.coroutines.*
//import platform.darwin.dispatch_async
//import platform.darwin.dispatch_get_global_queue
//import platform.darwin.dispatch_get_main_queue
//import kotlin.coroutines.CoroutineContext
//import kotlin.native.concurrent.*
//
////@ThreadLocal
//actual object ConcurrenceUtils {
//    fun freeze(obj: Any) {
//        obj.freeze()
//    }
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
//    fun<T> getAtomicReference(initial: T): AtomicRef<T> {
//        return atomic(initial)
//    }
//
//    actual fun <T> getAtomicReference(initial: T): T {
//        return atomic(initial).value
//    }
//
//    fun dispatchOnMain(block: Runnable) {
//        MainScope().launch {
//            block.run()
//        }
//    }
//
//    fun dispatchOnOther(block: Runnable) {
//        OtherScope().launch {
//            block.run()
//        }
//    }
//}
//
////@ThreadLocal
//class MainDispatcher: CoroutineDispatcher() {
//    override fun dispatch(context: CoroutineContext, block: Runnable) {
//        dispatch_async(dispatch_get_main_queue()) { block.run() }
//    }
//}
//
////@ThreadLocal
//class OtherDispatcher: CoroutineDispatcher() {
//    override fun dispatch(context: CoroutineContext, block: Runnable) {
//        dispatch_async(dispatch_get_global_queue(0, 0)) { block.run() }
//    }
//}
//
////@ThreadLocal
//class MainScope: CoroutineScope {
//    private val dispatcher = MainDispatcher()
//    private val job = Job()
//
//    override val coroutineContext: CoroutineContext
//        get() = dispatcher + job
//}
//
////@ThreadLocal
//class OtherScope: CoroutineScope {
//    private val dispatcher = OtherDispatcher()
//    private val job = Job()
//
//    override val coroutineContext: CoroutineContext
//        get() = dispatcher + job
//}