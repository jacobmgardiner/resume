package com.yoloapps.airmouse

data class ConnectionResponse(
    val status: Int,
    val msg: String,
    val screenDimension: IntArray = intArrayOf(0, 0),
//    val volume: Float = 0.5f,
    val appList: Array<String> = arrayOf()
) {
    companion object {
        const val STATUS_SUCCESS = 0
        const val MSG_SUCCESS = "Success!"
        const val STATUS_AUTH_ERROR = 1
        const val MSG_AUTH_ERROR = "Authentication Error! Password incorrect!"
        const val STATUS_ERROR = 2
        const val MSG_ERROR = "Error! Didn't recognize request!"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConnectionResponse

        if (status != other.status) return false
        if (msg != other.msg) return false
        if (!screenDimension.contentEquals(other.screenDimension)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = status
        result = 31 * result + msg.hashCode()
        result = 31 * result + screenDimension.contentHashCode()
        return result
    }
}