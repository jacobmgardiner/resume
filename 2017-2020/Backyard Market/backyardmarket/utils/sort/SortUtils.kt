package com.yoloapps.backyardmarket.utils.sort

import java.util.Comparator

object SortUtils {
    fun <T> binarySort(data: ArrayList<T>, new: T, c: Comparator<T>): Int {
        if(data.size == 0) {
            return 0
        }
        var index = -1
        var max = data.size
        var min = 0
        var comp: Int
        var mid: Int
        while(max - min > 1) {
            mid = (max + min) / 2
            comp = c.compare(new, data[mid])
            when {
                comp < 0 -> {
                    max = mid
                }
                comp > 0 -> {
                    min = mid
                }
                else -> {
                    return mid
                }
            }
        }
        index = if(c.compare(new, data[max-1]) > 0)
            max
        else
            min
        return index;
    }
}