package com.yoloapps.airmouse

import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import java.io.PrintWriter
import java.net.Socket
import java.net.SocketException
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class ServerHelper(val socket: Socket, val inputManager: InputManager) : Thread() {
    companion object {
        const val MAX_INPUT_RATE = 1000f
        const val MIN_INPUT_RATE = 30f
    }
    lateinit var writer: PrintWriter
    lateinit var scanner: Scanner
    private var done: AtomicBoolean = AtomicBoolean(false)
    var inputRate: Float = 60f

    private var _status: MutableStateFlow<ServerStatus> = MutableStateFlow(ServerStatus.RUNNING)
    var status = _status.asStateFlow()
    enum class ServerStatus() {
        WAITING_FOR_RESPONSE,
        RUNNING,
        ERROR,
        DISCONNECTING,
        DISCONNECTED,
    }
    fun updateStatus(status: ServerStatus) {
        _status.value = status
    }

    override fun run() {
        writer = PrintWriter(socket.getOutputStream())
        scanner = Scanner(socket.getInputStream())

        println("socket: ${socket.inetAddress.hostName}")
        println("waiting to send request...")
        sleep(2000)

        updateStatus(ServerStatus.WAITING_FOR_RESPONSE)
        val res = connect()
        when (res?.status) {
            ConnectionResponse.STATUS_SUCCESS -> {
                updateStatus(ServerStatus.RUNNING)
                ConnectionHelper.updateStatus(ConnectionHelper.ConnectionStatus.CONNECTED)
                println("Connected to server!!!!!")
            }
            ConnectionResponse.STATUS_AUTH_ERROR -> {
                updateStatus(ServerStatus.ERROR)
                ConnectionHelper.errorMsg = res.msg
                ConnectionHelper.updateStatus(ConnectionHelper.ConnectionStatus.AUTH_ERROR)
            }
            else -> {
                updateStatus(ServerStatus.ERROR)
                ConnectionHelper.updateStatus(ConnectionHelper.ConnectionStatus.FAILED)
                disconnect()
            }
        }
        res?.let { inputManager.screenRatio = it.screenDimension[0].toFloat() / it.screenDimension[1].toFloat() }
        res?.let { inputManager.initialVolume = it.volume }
        res?.let { inputManager.appList = it.appList }

//        val delay = (1000 / inputRate).toLong()
        val delay = (1000 / 60f).toLong()
        while (!done.get()) {
//            try {
                sendInput()
                sleep(delay)
//            } catch (e: SocketException) {
//                updateStatus(ServerStatus.ERROR)
//            }
        }
    }

    private fun sendInput() {
        SocketUtils.sendObject(writer,
            InputUpdate(
                x = inputManager.x,
                y = inputManager.y,
                button0 = inputManager.button0,
                button1 = inputManager.button1,
                button2 = inputManager.button2,
                inputString = inputManager.getStringInput(),
                backspace = inputManager.getBackspace(),
                enter = inputManager.getEnter(),
                volume = inputManager.getVolume(),
                appLaunch = inputManager.getAppToLaunch(),
            )
        )
    }

    private fun connect(): ConnectionResponse? {
        println("sending request")
        SocketUtils.sendObject(writer, ConnectionRequest(
            password = ConnectionHelper.password
        ))
        println("sent request")

        val rawRes = SocketUtils.getNextObject(scanner, ConnectionResponse::class)
        if (rawRes == null) { println("ERROR: response was null!");return null }
        val res = rawRes as ConnectionResponse
        println(res.msg)
        println(res.screenDimension)
        return res
    }

    fun disconnect() {
        updateStatus(ServerStatus.DISCONNECTING)
        done.set(true)
        onConnectionEnded()
    }

    private fun onConnectionEnded() {
        println("connection closed")
        socket.close()
        updateStatus(ServerStatus.DISCONNECTED)
    }
}