package com.yoloapps.reactiontimetracker

import com.yoloapps.reactiontimetracker.data.ObservableData
import com.yoloapps.reactiontimetracker.data.TestData
import com.yoloapps.reactiontimetracker.data.db.Prompt
import kotlinx.datetime.Clock
import kotlinx.coroutines.*
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class Test {
    companion object {
//        const val NUMBER_OF_PROMPTS = 120
        const val NUMBER_OF_PROMPTS = 70
//        const val NUMBER_OF_PROMPTS = 37
//        const val NUMBER_OF_PROMPTS = 6
//        const val NUMBER_OF_PROMPTS = 3

//        const val DURATION = 600
        const val DURATION = 300
//        const val DURATION = 180
//        const val DURATION = 30
//        const val DURATION = 7

        const val MIN_SPACE = 2000L
        const val MAX_SPACE = 10000L
        const val avgPromptDuration: Long = DURATION * 1000L / NUMBER_OF_PROMPTS
    }

    private var ended = false

    val elapsedTime = ObservableData<Long>()

    val data get() = toData()
//    private val listeners = ArrayList<TestEventListener>()
    val listeners = ArrayList<TestEventListener>()

    var startTime: Long? = null
    set(value) {
        field = value
        value?.let {
            for (i in 0 until prompts.size) {
                prompts[i] = Prompt(value, prompts[i].number, prompts[i].time, prompts[i].response_time)
            }
        }
    }
    var prompts: MutableList<Prompt> = generatePromptTimes() as MutableList<Prompt>
    var nextPromptNumber = 0
    var score = -1L

//    fun store() {
//
//    }

    private fun toData(): TestData {
        return TestData(
            com.yoloapps.reactiontimetracker.data.db.Test(
                startTime ?: -1,
                score,
            ),
            prompts
        )
    }

    /*suspend*/ fun start() {
        println("[TEST] STARTING")
        onStart()
        runTest()
    }

    var lastPromptTime = 0L
    private fun runTest() {
        startTime = Clock.System.now().toEpochMilliseconds()
//        prompts.forEach { it.date = startTime }
        println("[TEST] STARTED AT: $startTime")
        while (!isDone()) {
            elapsedTime.data = Clock.System.now().toEpochMilliseconds() - startTime!! - lastPromptTime
            if (Clock.System.now().toEpochMilliseconds() - startTime!! >=
                (prompts[nextPromptNumber].time ?: -1)
            ) {
                lastPromptTime = prompts[nextPromptNumber].time
                onPrompt()
                nextPromptNumber++
            }
        }
    }

    fun onTap() {
        println("[TEST] TAPPED")
        if (startTime == null) return
        if (nextPromptNumber < 1 || prompts[nextPromptNumber - 1].response_time != null) return

//        prompts[nextPromptNumber-1].response_time =
//            (Clock.System.now().toEpochMilliseconds() - startTime!! - prompts[nextPromptNumber-1].time)

        prompts[nextPromptNumber-1] = Prompt(
            prompts[nextPromptNumber-1].date,
            prompts[nextPromptNumber-1].number,
            prompts[nextPromptNumber-1].time,
            (Clock.System.now().toEpochMilliseconds() - startTime!! - prompts[nextPromptNumber-1].time),
        )

        listeners.forEach {
            it.onPromptEnd(prompts[nextPromptNumber])
        }
        if (isDone()) {
            onEnd()
        }
    }

    private fun isDone(): Boolean {
        return nextPromptNumber >= NUMBER_OF_PROMPTS
    }

    private fun onPrompt() {
        println("[TEST] ON PROMPT")
        println("[TEST] CALLING LISTENERS: ")
        listeners.forEach {
            println("   [TEST] $it")
            it.onPrompt(prompts[nextPromptNumber])
        }
    }

    private fun onStart() {
        println("[TEST] ON START")
        println("[TEST] CALLING LISTENERS: ")
        listeners.forEach {
            println("   [TEST] $it")
            it.onStart()
        }
    }

    private fun onEnd() {
        println("[TEST] ON END")
        calculateScore()
        ended = true
        println("[TEST] CALLING LISTENERS: ")
        listeners.forEach {
            println("   [TEST] $it")
            it.onEnd()
        }
    }

    private fun calculateScore() {
        //TODO("more scientific")
        var sum = 0L
        prompts.forEach {
            sum += it.response_time ?: 0L
        }
        score = sum / prompts.size
    }

    private fun generatePromptTimes(): List<Prompt> {
        var time = 0L
        println("[TEST] GENERATING TIMES")
        return List(NUMBER_OF_PROMPTS) {
//            println("index: $it")
            if (it == NUMBER_OF_PROMPTS - 1) time = DURATION * 1000L
            else {
                val space = Random.nextLong(
                    max(MIN_SPACE, DURATION * 1000L - time - MAX_SPACE * (NUMBER_OF_PROMPTS - it - 1)),
                    min(MAX_SPACE, DURATION * 1000L - time - MIN_SPACE * (NUMBER_OF_PROMPTS - it - 1))
                )
                time += space
            }
            Prompt(startTime ?: -1, it, time, null)
        }
    }

    fun addEventListener(listener: TestEventListener) {
        println("[TEST] ADDING LISTENER: $listener")
        listeners.add(listener)
    }

    fun isEnded(): Boolean {
        return ended
    }

    fun generateFakeData(date: Long) {
        startTime = date
        for (i in 0 until prompts.size) {
            prompts[i] = Prompt(
                prompts[i].date,
                prompts[i].number,
                prompts[i].time,
                Random.nextLong(100L, 500L),
            )
        }
        calculateScore()
    }
}