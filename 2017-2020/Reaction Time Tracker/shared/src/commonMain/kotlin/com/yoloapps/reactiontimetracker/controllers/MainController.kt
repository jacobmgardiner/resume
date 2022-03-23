package com.yoloapps.reactiontimetracker.controllers

import com.yoloapps.reactiontimetracker.Application
import com.yoloapps.reactiontimetracker.CSVUtil
import com.yoloapps.reactiontimetracker.DateUtils
import com.yoloapps.reactiontimetracker.data.db.Log

class MainController: ViewController() {
    val app by lazy { Application }

    //TODO
    val currentLog get() = app.repo.getLogsByRange(DateUtils.getDayRange(DateUtils.getCurrentDateAsMs())).firstOrNull()

    fun onRecord(quality: Int, hours: Int, note: String) {
        app.repo.storeLog(Log(DateUtils.getCurrentDateAsMs(), quality, hours, note))
    }

    fun onSend() {
        Application.fileUtils.sendFile(CSVUtil.exportToCsvFile())
    }
}