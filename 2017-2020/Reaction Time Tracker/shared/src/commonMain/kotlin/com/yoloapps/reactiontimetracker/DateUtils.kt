package com.yoloapps.reactiontimetracker

import kotlinx.datetime.*
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object DateUtils {
    const val HOUR_MS = 3600000L
    const val DAY_MS = 86400000L
    const val WEEK_MS = 604800000L
    const val MONTH_MS = 2628000000L
    const val YEAR_MS = 31540000000L
    
    fun getCurrentDateAsMs(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }

    fun getDayRange(date: Long): List<Long> {
        (date - date % DAY_MS).also {
            return listOf(it, it + DAY_MS)
        }
    }

    fun getWeekRange(date: Long): List<Long> {
        (date - date % WEEK_MS).also {
            return listOf(it, it + WEEK_MS)
        }
    }

    fun getMonthRange(date: Long): List<Long> {
        (date - date % MONTH_MS).also {
            return listOf(it, it + MONTH_MS)
        }
    }

    fun getYearRange(date: Long): List<Long> {
        (date - date % YEAR_MS).also {
            return listOf(it, it + YEAR_MS)
        }
    }

    fun dateToMs(year: Int, month: Int, day: Int): Long {
//        println("XXXXXX MONTH TO MS: $month")
        //month number not ordinal required
        return LocalDateTime(year, month + 1, day, 23, 0, 0, 0).toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    fun dateFromMs(date: Long): List<Int> {
        Instant.fromEpochMilliseconds(date).toLocalDateTime(TimeZone.currentSystemDefault()).also {
//            println("XXXXXX MONTH FROM MS: ${it.month.ordinal}")
            return listOf(it.year, it.month.ordinal, it.dayOfMonth)
        }
    }
}