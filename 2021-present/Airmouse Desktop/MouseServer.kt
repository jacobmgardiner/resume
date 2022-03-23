import java.awt.Toolkit
import java.io.PrintWriter
import java.net.Socket
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class MouseServer(val socket: Socket) : Thread() {
    lateinit var writer: PrintWriter
    lateinit var scanner: Scanner
    lateinit var client: Client
    val inputManager by lazy { InputManager() }
    private var done: AtomicBoolean = AtomicBoolean(false)
    val dimension by lazy { Toolkit.getDefaultToolkit().screenSize }

    override fun run() {
//        socket.keepAlive = true
        println("server thread started")
        writer = PrintWriter(socket.getOutputStream())
        scanner = Scanner(socket.getInputStream())
        println("waiting for connection request...")
        client = processConnectionRequest()
        ServerStatus.CONNECTED.msg = "Device ${client.name} connected"
        Main.updateStatus(ServerStatus.CONNECTED)
        println(ServerStatus.CONNECTED.msg)
        while (!done.get()) {
            val input = SocketUtils.getNextObject(scanner, InputUpdate::class) as InputUpdate?
            if (input == null) {
                println("getNextObject returned null")
                Main.stop()
            } else {
//                println(input)
                inputManager.onUpdate(input)
            }
        }
    }

    private fun processConnectionRequest(): Client {
        val req = SocketUtils.getNextObject(scanner, ConnectionRequest::class) as ConnectionRequest?
        println("received request: ${req?.password}")
        return if (req == null) {
            SocketUtils.sendObject(writer, ConnectionResponse(ConnectionResponse.STATUS_ERROR, ConnectionResponse.MSG_ERROR))
            println(ConnectionResponse.MSG_ERROR)
            processConnectionRequest()
        } else if (!AuthManager.validateHash(req.password)) {
            SocketUtils.sendObject(writer, ConnectionResponse(ConnectionResponse.STATUS_AUTH_ERROR, ConnectionResponse.MSG_AUTH_ERROR))
            println(ConnectionResponse.MSG_AUTH_ERROR)
            processConnectionRequest()
        } else {
            SocketUtils.sendObject(writer, ConnectionResponse(
                status = ConnectionResponse.STATUS_SUCCESS,
                msg = ConnectionResponse.MSG_SUCCESS,
                screenDimension = intArrayOf(dimension.width, dimension.height),
                appList = Main.appList.toTypedArray()
            ))
            println(ConnectionResponse.MSG_SUCCESS)
            Client(socket.inetAddress.hostName, System.currentTimeMillis())
        }
    }

    fun disconnect() {
        done.set(true)
        onConnectionEnded()
    }

    private fun onConnectionEnded() {
        socket.close()
        Main.updateStatus(ServerStatus.STOPPED)
    }
}