package com.yoloapps.reactiontimetracker.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LogActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_DATE = "date"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
    }
}