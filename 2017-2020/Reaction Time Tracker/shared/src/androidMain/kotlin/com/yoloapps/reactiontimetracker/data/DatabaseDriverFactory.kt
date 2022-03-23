package com.yoloapps.reactiontimetracker.data

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.yoloapps.reactiontimetracker.data.db.TestsDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(TestsDatabase.Schema, context, "test.db")
    }
}