package com.yoloapps.reactiontimetracker

import com.yoloapps.reactiontimetracker.data.DatabaseDriverFactory
import com.yoloapps.reactiontimetracker.data.Repository
import kotlin.jvm.JvmStatic
import kotlin.jvm.Synchronized
import kotlin.jvm.Volatile
import kotlin.native.concurrent.ThreadLocal

//private var instance: Application? = null
//@JvmStatic
//@Synchronized
//fun getInstance(): Application? {
//    instance?.let {
//        instance = Application()
//    }
//    return instance
//}

//const val developer: Boolean = true
//const val developer: Boolean = false

@ThreadLocal
object Application {
    const val developer: Boolean = true
    //const val developer: Boolean = false

    lateinit var repo: Repository
    lateinit var fileUtils: FileUtils

    fun onStart(databaseDriverFactory: DatabaseDriverFactory, fileUtilsFactory: FileUtilsFactory) {
        println("TESTING...")
        repo = Repository(databaseDriverFactory)
        fileUtils = fileUtilsFactory.getInstance()
//        repo.deleteAll()
    }
}