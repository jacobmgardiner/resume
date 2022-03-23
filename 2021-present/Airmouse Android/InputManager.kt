package com.yoloapps.airmouse

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener2
import android.hardware.SensorManager
import android.view.KeyEvent
import com.google.ar.sceneform.math.Quaternion
import java.util.*
import kotlin.math.cos

//import java.awt.Robot

class InputManager(private val context: Context) : SensorEventListener2 {
    companion object {
        @Volatile private var INSTANCE: InputManager? = null
        fun getInstance(context: Context): InputManager =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: InputManager(context).also { INSTANCE = it }
            }

//        const val ANGLE_Y = 90
//        const val ANGLE_X = 90
        const val SAMPLING_PERIOD = SensorManager.SENSOR_DELAY_GAME
    }

    var x = 0.5f
    var y = 0.5f
    var button0 = 0
    var button1 = 0
    var button2 = 0
    private var inputString = ""
    private var backspace = 0
    private var enter = 0
    private var volume = -1f
    private var appToLaunch = -1

    init {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

        sensorManager.registerListener(this, sensor, SAMPLING_PERIOD)
    }

    private var offsetOrientation = Quaternion()
    private var trueOrientation = Quaternion()

    var sensitivity = 2f
    var screenRatio: Float = 1f
    var initialVolume: Float = 0.5f
    var appList: Array<String> = arrayOf()

    override fun onSensorChanged(event: SensorEvent?) {
        //TODO("filter out more noise/stabilize?")
        //TODO("fix weird wrap around issue")
        event?.let {
            val vector = event.values.clone()
            trueOrientation = Quaternion(vector[3], vector[0], vector[1], vector[2])
            val orientation = Quaternion.multiply(trueOrientation, offsetOrientation.inverted())
            this.x = (orientation.x) * sensitivity + 0.5f
            this.y = (-orientation.z) * screenRatio * sensitivity + 0.5f
        }
    }

    fun offset() {
        offsetOrientation = trueOrientation
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//        TODO("Not yet implemented")
    }

    override fun onFlushCompleted(sensor: Sensor?) {
//        TODO("Not yet implemented")
    }

    fun appendString(str: String) {
        println("appending: $str to $inputString")
        inputString+=str
        println("resulting input string: $inputString")
    }

    fun getStringInput(): String {
        val str = inputString
        inputString = ""
        return str
    }

    fun backspace() {
        backspace = 1
    }
    fun getBackspace(): Int {
        val b = backspace
        backspace = 0
        return b
    }

    fun enter() {
        enter = 1
    }
    fun getEnter(): Int {
        val e = enter
        enter = 0
        return e
    }

    fun changeVolume(vol: Float) {
        volume = vol
    }

    fun getVolume(): Float {
        val v = volume
        volume = -1f
        return v
    }

    fun launchApp(appIdx: Int) {
        appToLaunch = appIdx
    }

    fun getAppToLaunch(): Int {
        val a = appToLaunch
        appToLaunch = -1
        return a
    }
}