package com.yoloapps.reactiontimetracker.controllers

import com.yoloapps.reactiontimetracker.Application

class PostTestController : ViewController() {
    val app by lazy { Application }

    val test get() = app.repo.getLastTest()?.test
}