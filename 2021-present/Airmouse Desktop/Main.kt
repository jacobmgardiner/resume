// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.jetbrains.skija.Bitmap
import java.awt.Color
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException

@Composable
@Preview
fun App() {
    DesktopMaterialTheme {
        Column(
            modifier = Modifier
                .verticalScroll(enabled = true, state = ScrollState(0))
//                .horizontalScroll(enabled = true, state = ScrollState(0))
                .fillMaxSize()
                .padding(8.dp, 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Air Mouse", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(32.dp))
            val status = Main.status.collectAsState(ServerStatus.STOPPED)
            Button(onClick = {
                when(status.value) {
                    ServerStatus.STOPPED -> Main.onStart()
                    ServerStatus.WAITING -> Main.cancel()
                    ServerStatus.CONNECTED -> Main.stop()
                    ServerStatus.PAUSED -> Main.togglePause()
                    else -> Main.onStart()
                }
            }) {
                Text(when(status.value) {
                    ServerStatus.STOPPED -> "Start Server"
                    ServerStatus.WAITING -> "Cancel"
                    ServerStatus.CONNECTED -> "Stop Server"
                    ServerStatus.PAUSED -> "Resume Server"
                    else -> "Start Server"
                })
            }
            Spacer(Modifier.height(4.dp))
            Text(status.value.msg)

            Column(
//                Modifier
//                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(16.dp))
                Text("Host IP address: ${Main.hostIp}", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                QRCode(Main.hostIp, 150, 21)
//                QRCode(Main.hostIp, 150, 512)
            }

            Row(
                modifier = Modifier
                    .horizontalScroll(enabled = true, state = ScrollState(0))
                    .fillMaxWidth()
//                    .weight(1f)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,

            ) {
                Text("Password: ", fontWeight = FontWeight.Bold)
                val password = remember { mutableStateOf(TextFieldValue(/*text = AuthManager.storedHash.toString()*/)) }
                TextField(
                    password.value,
                    { password.value = it },
                    Modifier.padding(4.dp),
                )
                Button(onClick = {
                    AuthManager.setConnectionPassword(password.value.text)
                }) {
                    Text("Update")
                }
            }
//            Spacer(Modifier.weight(0.5f))
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    Window(onCloseRequest = { Main.stop();exitApplication() }, /*icon = BitmapPainter(), */title = "Air Mouse", onKeyEvent = {
        //TODO("check up vs down")
        if (it.isCtrlPressed) {
            if (it.key == Key.Q) {
                Main.stop()
            } else if (it.key == Key.P) {
                Main.togglePause()
            }
        }
        return@Window false
    }) {
        App()
    }
}

enum class ServerStatus(var msg: String) {
    STOPPED(""),
    WAITING("Waiting for connection..."),
    CONNECTED("Device connected"),
    CANCELLED("Device search cancelled"),
    PAUSED("Remote input paused"),
}

object Main {
    var hostIp: String = InetAddress.getLocalHost().hostAddress
    private const val PORT = 6666

    var appList = arrayListOf<String>()
    var appListPaths = arrayListOf<String>()

    private val _status: MutableStateFlow<ServerStatus> = MutableStateFlow(ServerStatus.STOPPED)
    val status: StateFlow<ServerStatus> = _status.asStateFlow()

    fun updateStatus(status: ServerStatus) {
        if (status == ServerStatus.CONNECTED) status.msg = "Device ${socket?.inetAddress?.hostName} connected"
        _status.value = status
    }

    var serverSocket: ServerSocket? = null
    var socket: Socket? = null
    var server: MouseServer? = null

    fun onStart() /*= runBlocking*/ {
        CoroutineScope(Dispatchers.Default).launch {
            _status.value = ServerStatus.WAITING

            try {
                serverSocket = ServerSocket(PORT)
            } catch (e: java.net.BindException) {
                e.printStackTrace()
                _status.value = ServerStatus.STOPPED
                return@launch
            }
            try {
                socket = serverSocket!!.accept()
            } catch (e: SocketException) {
                e.printStackTrace()
                _status.value = ServerStatus.STOPPED
                return@launch
            }

            server = MouseServer(socket!!)
            server!!.start()
        }
    }

    fun togglePause() {
        if (status.value == ServerStatus.CONNECTED || status.value == ServerStatus.PAUSED) {
            if (server?.inputManager?.togglePause() == true) {
                _status.value = ServerStatus.PAUSED
            } else _status.value = ServerStatus.CONNECTED
        }
    }

    fun stop() = runBlocking {
        println("disconnecting...")
        serverSocket?.close()
        socket?.close()
        server?.disconnect()
        updateStatus(ServerStatus.STOPPED)
        println("disconnected")
    }

    fun cancel() = runBlocking {
        println("cancelling socket...")
        serverSocket?.close()
        socket?.close()
        println("cancelled socket")
    }
}