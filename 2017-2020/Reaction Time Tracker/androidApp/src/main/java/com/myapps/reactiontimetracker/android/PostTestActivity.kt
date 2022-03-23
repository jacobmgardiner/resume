package com.yoloapps.reactiontimetracker.android

import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import com.yoloapps.reactiontimetracker.ControlledActivity
import com.yoloapps.reactiontimetracker.DateUtils
import com.yoloapps.reactiontimetracker.Test
import com.yoloapps.reactiontimetracker.android.databinding.ActivityPostTestBinding
import com.yoloapps.reactiontimetracker.controllers.PostTestController
import java.util.*

class PostTestActivity : ControlledActivity<PostTestController>() {
    val binding by lazy { ActivityPostTestBinding.inflate(layoutInflater) }

    override fun onInstantiateController(): PostTestController {
        return PostTestController()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.next.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)) }

        binding.score.text = controller.test?.score.toString()

        //TODO
        val bundle = bundleOf(
            ChartFragment.PARAM_START_TIME to controller.test?.date,
            ChartFragment.PARAM_END_TIME to (controller.test?.date?.plus(Test.DURATION * 1000L) ?: 0),
            ChartFragment.PARAM_DATA_SETS to EnumSet.of(ChartFragment.DataSetTypes.PROMPTS),
            ChartFragment.PARAM_DATE_PICKER_ENABLED to false,
        )
        val chartFrag = ChartFragment()
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.chart, chartFrag.also {it.arguments = bundle})
        }
    }
}