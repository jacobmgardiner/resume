package com.yoloapps.reactiontimetracker.data

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.yoloapps.reactiontimetracker.data.db.TestsDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(TestsDatabase.Schema, "test.db")
    }
}