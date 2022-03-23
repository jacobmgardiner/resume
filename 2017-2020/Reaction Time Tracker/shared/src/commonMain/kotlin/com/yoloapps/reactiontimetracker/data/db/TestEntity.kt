package com.yoloapps.reactiontimetracker.data.db

import kotlinx.serialization.Serializable

@Serializable
data class TestEntity(
    var date: Long,
    var score: Long,
)