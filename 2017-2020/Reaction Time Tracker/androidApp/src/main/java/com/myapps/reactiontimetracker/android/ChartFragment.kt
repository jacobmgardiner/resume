package com.yoloapps.reactiontimetracker.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yoloapps.reactiontimetracker.Application
import com.yoloapps.reactiontimetracker.ControlledFragment
import com.yoloapps.reactiontimetracker.DateUtils
import com.yoloapps.reactiontimetracker.android.databinding.FragmentChartBinding
import com.yoloapps.reactiontimetracker.controllers.ChartController
import com.yoloapps.reactiontimetracker.data.DataObserver
import com.yoloapps.reactiontimetracker.data.ObservableData
import java.util.*

class ChartFragment(/*val startTime: Long, val endTime: Long, val dataSets: EnumSet<DatasetTypes>*/) : ControlledFragment<ChartController>() {
    companion object {
        const val PARAM_START_TIME = "startTime"
        const val PARAM_END_TIME = "endTime"
        const val PARAM_DATA_SETS = "dataSets"
        const val PARAM_MARKER_SPACING = "markerSpacing"
        const val PARAM_DATE_PICKER_ENABLED = "pickerEnabled"
    }

    enum class DataSetTypes {
        SCORES,
        QUALITY,
        HOURS,
        PROMPTS,

//            val ALL_BUT_PROMPTS: EnumSet<DatasetTypes> = EnumSet.complementOf(EnumSet.of(PROMPTS))
    }

    val app by lazy { Application }

    var endTime = DateUtils.getCurrentDateAsMs()
    var startTime = endTime - DateUtils.WEEK_MS
    var dataSets = EnumSet.of(DataSetTypes.SCORES, DataSetTypes.QUALITY, DataSetTypes.HOURS)
    var markerSpacing = 100L
    var datePickerEnabled = true

    private var _binding: FragmentChartBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val chartView by lazy { ChartView(requireContext()) }

    override fun onInstantiateController(): ChartController {
        return ChartController()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)

        binding.chartViewContainer.addView(chartView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startTime = requireArguments().getLong(PARAM_START_TIME, startTime)
        endTime = requireArguments().getLong(PARAM_END_TIME, endTime)
        (requireArguments().get(PARAM_DATA_SETS) as EnumSet<DataSetTypes>?)?.let { dataSets = it }
        markerSpacing = requireArguments().getLong(PARAM_MARKER_SPACING, markerSpacing)
        datePickerEnabled = requireArguments().getBoolean(PARAM_DATE_PICKER_ENABLED, datePickerEnabled)

        chartView.measured.addObserver(object : DataObserver<Boolean>() {
            override fun onUpdate(data: Boolean) {
                setupChart()
            }
        })

        setupDatePicker()
    }

    private fun setupDatePicker() {
        if(!datePickerEnabled) {
            binding.datePickerContainer.visibility = View.GONE
//            binding.setStart.visibility = View.GONE
            return
        }

        binding.setStart.setOnClickListener {
            DatePickerFragment(startTime).also {
                it.date.addObserver(object : DataObserver<Long>() {
                    override fun onUpdate(data: Long) {
                        controller.changeStartAll(data)
                    }
                })
            }.show(childFragmentManager, "datePicker")
        }

        binding.setEnd.setOnClickListener {
            DatePickerFragment(endTime).also {
                it.date.addObserver(object : DataObserver<Long>() {
                    override fun onUpdate(data: Long) {
                        controller.changeEndAll(data)
                    }
                })
            }.show(childFragmentManager, "datePicker")
        }
    }

    private fun setupChart() {
        controller.pixelWidth = chartView.measuredWidth
        controller.pixelHeight = chartView.measuredHeight

        val range = listOf(startTime, endTime)

        if (dataSets.contains(DataSetTypes.SCORES)) {
            controller.addDataSet(controller.extractTestData(app.repo.getTestsByRange(startTime, endTime)), range, listOf(0,800))
        }
        var logData: List<List<List<Long>>>? = null
        if (dataSets.contains(DataSetTypes.QUALITY)) {
            logData = controller.extractLogData(app.repo.getLogsByRange(startTime, endTime))
            controller.addDataSet(logData[0], range, listOf(0,11))
        }
        if (dataSets.contains(DataSetTypes.HOURS)) {
            if (logData == null) logData = controller.extractLogData(app.repo.getLogsByRange(startTime, endTime))
            controller.addDataSet(logData[1], range, listOf(0,16))
        }
        if (dataSets.contains(DataSetTypes.PROMPTS)) {
            controller.addDataSet(controller.extractPromptsData(app.repo.getPromptsByRange(startTime, endTime)), range, listOf(0,800))
        }
    }

    inner class ChartView(val chartContext: Context) : View(chartContext) {
        init {
            //TODO("figure out size stuffs")
            //TODO("figure out attribute stuffs")

            controller.isInvalid.addObserver(object : DataObserver<Boolean>() {
                override fun onUpdate(data: Boolean) {
                    invalidate()
                }
            })
        }

        val measured = ObservableData<Boolean>()

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            measured.data = true
        }

        var paint: Paint = Paint()
        var dashedLine = DashPathEffect(floatArrayOf(5f, 50f), 0f)
        val dashedPaint = Paint().also {
            it.strokeWidth = 3f
            it.pathEffect = dashedLine
            it.color = Color.BLACK
        }
        var lastPoint: List<Float>? = null

        override fun onDraw(canvas: Canvas?) {
            canvas?.let { c ->
                controller.dataSets.forEachIndexed { i, dataPoints ->
                    paint.color = controller.colors[i%controller.colors.size].toInt()
                    lastPoint = null
                    dataPoints.forEach {
                        controller.dataToPixel(it, i)?.let { point ->
                            paint.strokeWidth = 25f
                            c.drawPoint(point[0], point[1], paint)

                            paint.strokeWidth = 10f
                            lastPoint?.let { last -> c.drawLine(last[0], last[1], point[0], point[1], paint) }

                            lastPoint = point
                        }
                    }
                }

                //TODO
                val markerSpacing = 100L
                var markerHeight = markerSpacing
                while (markerHeight < 800) {
                    controller.dataToPixel(listOf(0L, markerHeight), 0)?.let {
                        c.drawLine(0f, it[1], width.toFloat(), it[1], dashedPaint)
                    }
                    markerHeight += markerSpacing
                }

                controller.isInvalid.data = false
            }
        }
    }
}