package com.yoloapps.reactiontimetracker.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@Serializable
data class PromptData(
    val date: Long,
    val number: Int,
    val time: Long,
//    @SerialName("response_time")
    var responseTime: Long? = null,
) {
    fun isResponseTimeValid(): Boolean {
        return responseTime ?: -1 < 100
    }

    fun setResponseTime(responseTime: Long) {
        this.responseTime = if (isResponseTimeValid()) responseTime else null
    }
}