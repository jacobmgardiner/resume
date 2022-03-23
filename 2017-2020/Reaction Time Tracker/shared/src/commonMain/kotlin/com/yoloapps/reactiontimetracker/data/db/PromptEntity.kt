package com.yoloapps.reactiontimetracker.data.db

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromptEntity(
    val date: Long,
    val number: Int,
    val time: Long,
    @SerialName("response_time")
    var responseTime: Long? = null,
)