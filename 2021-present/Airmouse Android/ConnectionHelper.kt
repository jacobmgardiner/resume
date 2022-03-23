package com.yoloapps.airmouse

import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import java.net.ConnectException
import java.net.Socket

object ConnectionHelper {
    lateinit var socket: Socket
    var server: ServerHelper? = null

    //TODO("make changeable by user")
    var host: String = "192.168.0.185"
    var password: String = "testing"
    var port = 6666

    private var _status: MutableStateFlow<ConnectionStatus> = MutableStateFlow(ConnectionStatus.NO_ATTEMPT)
    var status = _status.asStateFlow()

    fun updateStatus(status: ConnectionStatus) {
        _status.value = status
    }

    var hostName: String? = null
    var errorMsg: String? = null

    enum class ConnectionStatus(var msg: String) {
        NO_ATTEMPT(""),
        CONNECTING("Attempting to connect..."),
        INIT_SUCCESS("Connecting to: $hostName"),
        CONNECTED("Connected to: $hostName"),
        AUTH_ERROR("Couldn't authenticate: $errorMsg"),
        FAILED("Error connecting: $errorMsg"),
    }

    fun connect(inputManager: InputManager): Boolean {
        updateStatus(ConnectionStatus.CONNECTING)
        println("XXXX connecting to socket")
        try {
            socket = Socket(ConnectionHelper.host, ConnectionHelper.port)
        } catch (e: ConnectException) {
            errorMsg = e.message
            updateStatus(ConnectionStatus.FAILED)
            e.printStackTrace()
            return false
        }
        hostName = socket.inetAddress.hostName
        updateStatus(ConnectionStatus.INIT_SUCCESS)
        server = ServerHelper(socket, inputManager)
        server!!.start()
        return true
    }

//    suspend fun respondToConnectionEvents(navController: NavController?) {
//        status.collect {
//            when (it) {
//                ConnectionStatus.CONNECTED -> { navController?.navigate("main") }
//                else -> {  }
//            }
//        }
//    }
}