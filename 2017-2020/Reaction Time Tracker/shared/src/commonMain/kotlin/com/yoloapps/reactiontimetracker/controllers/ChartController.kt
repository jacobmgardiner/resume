package com.yoloapps.reactiontimetracker.controllers

import com.yoloapps.reactiontimetracker.data.DataObserver
import com.yoloapps.reactiontimetracker.data.ObservableData
import com.yoloapps.reactiontimetracker.data.db.Log
import com.yoloapps.reactiontimetracker.data.db.Prompt
import com.yoloapps.reactiontimetracker.data.db.Test
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChartController : ViewController() {
//    private val _isInvalid = MutableStateFlow(false)
//    val isInvalid : StateFlow<Boolean> = _isInvalid

    val isInvalid = ObservableData<Boolean>().also { it.data = false }

    // red, blue, yellow, green
    val colors = listOf(0xffeb2626, 0xff428df5, 0xfff2f216, 0xff66de33)

    var pixelWidth: Int? = null
    var pixelHeight: Int? = null
    private var range = ArrayList<List<Long>>()
//        set(value) {
//            field = value
//            invalidateView()
//        }
    private var domain = ArrayList<List<Long>>()
//        set(value) {
//            field = value
//            invalidateView()
//        }
    var dataSets = ArrayList<List<List<Long>>>()
//        set(value) {
//            field = value
//            invalidateView()
//        }

    fun dataToPixel(dataPoint: List<Long>, dataSet: Int): List<Float>? {
        if (pixelWidth == null) return null
        return listOf(
            ((dataPoint[0] - (range!![dataSet][0])).toFloat() / (range!![dataSet][1] - range!![dataSet][0]).toFloat() * pixelWidth!!),
            pixelHeight!! - ((dataPoint[1] - (domain?.get(dataSet)?.get(0) ?: 0)).toFloat()* (pixelHeight?.div(((domain?.get(dataSet)?.get(1) ?: 0) - (domain?.get(dataSet)?.get(0) ?: 0)).toFloat()))!!),
        )
    }

    fun invalidateView() {
        //TODO("do platform specific stuff")
        println("XXXXX INVALIDATING!!!!!!!!!!!!!!!!!!")
        isInvalid.data = true
//        _isInvalid.value = true
    }

    fun extractTestData(tests: List<Test>): List<List<Long>> {
        return List(tests.size) {
            listOf(tests[it].date, tests[it].score)
        }
    }

    fun extractLogData(logs: List<Log>): List<List<List<Long>>> {
        return listOf(
            List(logs.size) {
                listOf(logs[it].date, logs[it].quality.toLong())
            },
            List(logs.size) {
                listOf(logs[it].date, logs[it].hours.toLong())
            },
        )
    }

    fun extractPromptsData(prompts: List<Prompt>): List<List<Long>> {
        return List(prompts.size) {
            listOf(prompts[it].date + prompts[it].time, prompts[it].response_time ?: 0)
        }
    }

    fun addDataSet(data: List<List<Long>>, range: List<Long>, domain: List<Long>) {
        dataSets.add(data)
        this.range.add(range)
        this.domain.add(domain)
        invalidateView()
    }

    fun changeRange(range: List<Long>, dataSet: Int) {
        this.range.add(dataSet, range)
        invalidateView()
    }
    fun changeRangeAll(range: List<Long>) {
        for (i in 0 until this.range.size)
            this.range[i] = range
        invalidateView()
    }
    fun changeStartAll(start: Long) {
        for (i in 0 until this.range.size)
            this.range[i] = listOf(start, this.range[i][1])
        invalidateView()
    }
    fun changeEndAll(end: Long) {
        for (i in 0 until this.range.size)
            this.range[i] = listOf(this.range[i][0], end)
        invalidateView()
    }

    fun changeDomain(domain: List<Long>, dataSet: Int) {
        this.domain.add(dataSet, domain)
        invalidateView()
    }
    fun changeDomainAll(domain: List<Long>) {
        for (i in 0 until this.domain.size)
            this.domain[i] = domain
        invalidateView()
    }
}