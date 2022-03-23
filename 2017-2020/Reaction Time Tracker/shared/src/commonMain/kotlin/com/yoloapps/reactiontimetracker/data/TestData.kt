package com.yoloapps.reactiontimetracker.data

import com.yoloapps.reactiontimetracker.data.db.Prompt
import com.yoloapps.reactiontimetracker.data.db.PromptEntity
import com.yoloapps.reactiontimetracker.data.db.Test
import com.yoloapps.reactiontimetracker.data.db.TestEntity

data class TestData(
    /**
     * date the test was started
     */
    var test: Test,
    var prompts: List<Prompt>,
) {
}