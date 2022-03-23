package com.yoloapps.reactiontimetracker.android

import android.app.Application
import com.yoloapps.reactiontimetracker.FileUtilsFactory
import com.yoloapps.reactiontimetracker.data.DatabaseDriverFactory

class ReactionTimeTrackerApplication : Application() {
    val app by lazy { com.yoloapps.reactiontimetracker.Application }

    override fun onCreate() {
        super.onCreate()
        app.onStart(DatabaseDriverFactory(this), FileUtilsFactory(this))
    }
}