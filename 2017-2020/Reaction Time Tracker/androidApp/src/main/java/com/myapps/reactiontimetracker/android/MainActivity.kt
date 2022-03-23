package com.yoloapps.reactiontimetracker.android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import com.yoloapps.reactiontimetracker.*
import com.yoloapps.reactiontimetracker.android.databinding.ActivityMainBinding
import com.yoloapps.reactiontimetracker.controllers.MainController
import com.yoloapps.reactiontimetracker.data.db.Log
import java.io.File
import java.util.*

class MainActivity : ControlledActivity<MainController>() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onInstantiateController(): MainController {
        return MainController()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        Application.onStart(DatabaseDriverFactory(this))

        setContentView(binding.root)

        binding.instructions.visibility = View.VISIBLE

        setupLog()

        binding.start.setOnClickListener { startActivity(Intent(this, TestActivity::class.java)) }

        binding.recordLog.setOnClickListener {
            controller.onRecord(binding.sleepQuality.progress, binding.sleepHours.progress, binding.logNotes.text.toString())
        }

        binding.sendButton.setOnClickListener {
            controller.onSend()
        }

        if(Application.developer) {
            binding.moreInfo.setOnClickListener { DeveloperUtils.fakeMonth() }
        }

        if (savedInstanceState != null) return
        setupChart()
    }

    private fun setupLog() {
        controller.currentLog?.let {
            binding.sleepQuality.progress = it.quality
            binding.sleepHours.progress = it.hours
            binding.logNotes.setText(it.note)
        }
    }

    private fun setupChart() {
        val bundle = bundleOf(
            ChartFragment.PARAM_START_TIME to DateUtils.getCurrentDateAsMs() - DateUtils.WEEK_MS,
            ChartFragment.PARAM_END_TIME to DateUtils.getCurrentDateAsMs(),
            ChartFragment.PARAM_DATA_SETS to EnumSet.of(ChartFragment.DataSetTypes.SCORES, ChartFragment.DataSetTypes.QUALITY, ChartFragment.DataSetTypes.HOURS),
        )
//        val chartFrag = ChartFragment(DateUtils.getCurrentDateAsMs() - DateUtils.WEEK_MS, DateUtils.getCurrentDateAsMs(), EnumSet.of(ChartFragment.DataSetTypes.SCORES, ChartFragment.DataSetTypes.QUALITY, ChartFragment.DataSetTypes.HOURS))
        val chartFrag = ChartFragment()
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.chartFragmentContainer, chartFrag.also {it.arguments = bundle})
        }
    }
}
