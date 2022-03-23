package com.yoloapps.reactiontimetracker.android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.yoloapps.reactiontimetracker.ControlledActivity
import com.yoloapps.reactiontimetracker.Test
import com.yoloapps.reactiontimetracker.TestEventListener
import com.yoloapps.reactiontimetracker.android.databinding.ActivityTestBinding
import com.yoloapps.reactiontimetracker.controllers.TestController
import com.yoloapps.reactiontimetracker.data.DataObserver
import com.yoloapps.reactiontimetracker.data.db.Prompt
import com.yoloapps.reactiontimetracker.data.db.PromptEntity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class TestActivity : ControlledActivity<TestController>() {
    override fun onInstantiateController(): TestController {
        return TestController()
    }

    val binding by lazy { ActivityTestBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.root.setOnClickListener {
            onTap()
        }

        controller.addTestEventListener(object : TestEventListener {
            override fun onPrompt(prompt: Prompt) {
                controller.onPrompt()
                runOnUiThread {
                    Log.d("XXX", "ON PROMPT")
                    binding.prompt.visibility = View.VISIBLE
                }
            }

            override fun onPromptEnd(prompt: Prompt) {
//                TODO("Not yet implemented")
            }

            override fun onStart() {
//                TODO("Not yet implemented")
                Log.d("XXX", "STARTED")
                controller.observableTimeElapsed.addObserver(object : DataObserver<Long>() {
                    override fun onUpdate(data: Long) {
                        runOnUiThread {
                            binding.time.text = data.toString()
                        }
                    }
                })
            }

            override fun onEnd() {
//                TODO("Not yet implemented")
                Log.d("XXX", "END")
                controller.onTestEnd()
                startActivity(Intent(this@TestActivity, PostTestActivity::class.java))
            }
        })
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch(IO) {
            controller.onStart()
        }
    }

    fun onTap() {
        controller.onTap()
        runOnUiThread {
            binding.prompt.visibility = View.GONE
        }
    }
}