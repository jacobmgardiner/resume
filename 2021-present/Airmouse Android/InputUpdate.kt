package com.yoloapps.airmouse

data class InputUpdate(
    val x: Float, //[0,1]
    val y: Float, //[0,1]
    val button0: Int = 0, //left
    val button1: Int = 0, //middle
    val button2: Int = 0, //right
    val inputString: String = "",
    val backspace: Int = 0,
    val enter: Int = 0,
    val volume: Float = -1f,
    val appLaunch: Int = -1,
)