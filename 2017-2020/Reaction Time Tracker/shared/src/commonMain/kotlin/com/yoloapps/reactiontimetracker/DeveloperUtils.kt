package com.yoloapps.reactiontimetracker

import com.yoloapps.reactiontimetracker.data.TestData
import com.yoloapps.reactiontimetracker.data.db.Log
import kotlin.native.concurrent.ThreadLocal
import kotlin.random.Random

@ThreadLocal
object DeveloperUtils {
    val app by lazy { Application }

    fun fakeData(range: List<Long>) {
        var date = range[0]
        while (date <= range[1]) {
            Test().also {
                it.generateFakeData(date)
                app.repo.storeTest(it.data)
            }
            app.repo.storeLog(Log(date, Random.nextInt(0, 11), Random.nextInt(0, 11), "AUTO-GENERATED LOG FOR TESTING PURPOSES"))

            date += DateUtils.DAY_MS
        }
    }

    fun fakeMonth() {
        fakeData(listOf(DateUtils.getCurrentDateAsMs() - DateUtils.MONTH_MS, DateUtils.getCurrentDateAsMs()))
    }
}