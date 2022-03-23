package com.yoloapps.backyardmarket.utils

import android.content.Context
import com.yoloapps.backyardmarket.R
import com.yoloapps.backyardmarket.data.classes.Type

object UnitUtils {
    fun unitConversionRatio(lastUnit: Int, unit: Int): Double {
        return when(lastUnit) {
            Type.UNIT_ITEM -> {
                return when(unit) {
                    Type.UNIT_ITEM -> 1.0
                    else -> 0.0
                }
            }
            Type.UNIT_OZ -> {
                return when(unit) {
                    Type.UNIT_OZ -> 1.0
                    Type.UNIT_LBS -> 1/16.0
                    else -> 0.0
                }
            }
            Type.UNIT_LBS -> {
                return when(unit) {
                    Type.UNIT_OZ -> 16.0
                    Type.UNIT_LBS -> 1.0
                    else -> 0.0
                }
            }
            Type.UNIT_FLOZ -> {
                return when(unit) {
                    Type.UNIT_FLOZ -> 1.0
                    Type.UNIT_PT -> 1/16.0
                    Type.UNIT_QT -> 1/32.0
                    Type.UNIT_GAL -> 1/128.0
                    else -> 0.0
                }
            }
            Type.UNIT_PT -> {
                return when(unit) {
                    Type.UNIT_FLOZ -> 16.0
                    Type.UNIT_PT -> 1.0
                    Type.UNIT_QT -> 0.5
                    Type.UNIT_GAL -> 1/8.0
                    else -> 0.0
                }
            }
            Type.UNIT_QT -> {
                return when(unit) {
                    Type.UNIT_FLOZ -> 32.0
                    Type.UNIT_PT -> 2.0
                    Type.UNIT_QT -> 1.0
                    Type.UNIT_GAL -> 1/4.0
                    else -> 0.0
                }
            }
            Type.UNIT_GAL -> {
                return when(unit) {
                    Type.UNIT_FLOZ -> 128.0
                    Type.UNIT_PT -> 8.0
                    Type.UNIT_QT -> 4.0
                    Type.UNIT_GAL -> 1.0
                    else -> 0.0
                }
            }
            else -> 0.0
        }
    }

    fun canConvert(unit: Int, newUnit: Int): Boolean {
        return when(unit) {
            Type.UNIT_ITEM -> {
                return when(newUnit) {
                    Type.UNIT_ITEM -> true
                    else -> false
                }
            }
            Type.UNIT_OZ -> {
                return when(newUnit) {
                    Type.UNIT_OZ -> true
                    Type.UNIT_LBS -> true
                    else -> false
                }
            }
            Type.UNIT_LBS -> {
                return when(newUnit) {
                    Type.UNIT_OZ -> true
                    Type.UNIT_LBS -> true
                    else -> false
                }
            }
            Type.UNIT_FLOZ -> {
                return when(newUnit) {
                    Type.UNIT_FLOZ -> true
                    Type.UNIT_QT -> true
                    Type.UNIT_PT -> true
                    Type.UNIT_GAL -> true
                    else -> false
                }
            }
            Type.UNIT_QT -> {
                return when(newUnit) {
                    Type.UNIT_FLOZ -> true
                    Type.UNIT_QT -> true
                    Type.UNIT_PT -> true
                    Type.UNIT_GAL -> true
                    else -> false
                }
            }
            Type.UNIT_PT -> {
                return when(newUnit) {
                    Type.UNIT_FLOZ -> true
                    Type.UNIT_QT -> true
                    Type.UNIT_PT -> true
                    Type.UNIT_GAL -> true
                    else -> false
                }
            }
            Type.UNIT_GAL -> {
                return when(newUnit) {
                    Type.UNIT_FLOZ -> true
                    Type.UNIT_QT -> true
                    Type.UNIT_PT -> true
                    Type.UNIT_GAL -> true
                    else -> false
                }
            }
            else -> false
        }
    }

    fun getUnitAsString(context: Context, unit: Int): String {
        return context.resources.getStringArray(R.array.units)[unit]
    }

    //    fun waitFor(checkRate: Long, timeout: Long, isDone: () -> Boolean): Boolean {
//        var result = false
//        var elapsedTime = 0L
//        val startTime = System.currentTimeMillis()
//        runBlocking {
//            while(timeout > elapsedTime) {
//                elapsedTime = System.currentTimeMillis() - startTime
//                Log.d("XXXXXxx", "WAITING FOR: " + elapsedTime)
//                delay(checkRate)
//                if (isDone()) {
//                    result = true
//                    return@runBlocking
//                }
//            }
//        }
//        return result
//    }
}