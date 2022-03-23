package com.yoloapps.reactiontimetracker

import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object CSVUtil {
    val app by lazy { Application }

    fun exportToCsvFile(): String {
        KMMFile(app.fileUtils.dataFileSaveLocation, "data.csv").also {
            it.createNewFile()

            it.writeText("testData")
            it.appendText("\n")
            it.appendText("time,score")
            it.appendText("\n")
            app.repo.getAllTests().forEach { test ->
                it.appendText("${test.date},${test.score}")
                it.appendText("\n")
            }

            it.appendText("\n")

            it.appendText("promptData")
            it.appendText("\n")
            it.appendText("date,number,time,response_time")
            it.appendText("\n")
            app.repo.getAllPrompts().forEach { prompt ->
                it.appendText("${prompt.date},${prompt.number},${prompt.time},${prompt.response_time}")
                it.appendText("\n")
            }

            it.appendText("\n")

            it.appendText("LogData")
            it.appendText("\n")
            it.appendText("date,quality,hours,note")
            it.appendText("\n")
            app.repo.getAllLogs().forEach { log ->
                it.appendText("${log.date},${log.quality},${log.hours},${log.note}")
                it.appendText("\n")
            }

            return it.absolutePath
        }
    }
}