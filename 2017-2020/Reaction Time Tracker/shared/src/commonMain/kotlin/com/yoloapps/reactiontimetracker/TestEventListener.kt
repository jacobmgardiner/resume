package com.yoloapps.reactiontimetracker

import com.yoloapps.reactiontimetracker.data.db.Prompt

interface TestEventListener {
    fun onPrompt(prompt: Prompt)
    fun onPromptEnd(prompt: Prompt)
    fun onStart()
    fun onEnd()
}