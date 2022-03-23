import org.mindrot.jbcrypt.BCrypt
import java.io.File
import java.io.PrintWriter
import java.util.*

object AuthManager {
    private val passwordFile = File("air_mouse_data")
    private var storedHash: String
        get() = Scanner(passwordFile.inputStream()).nextLine()
        set(value) = passwordFile.outputStream().write(value.encodeToByteArray())

    fun validateHash(password: String): Boolean {
//        println("checking: ${password} ${storedHash.toString()}")
        return BCrypt.checkpw(password, storedHash.toString())
    }

    fun setConnectionPassword(password: String) {
        storedHash = BCrypt.hashpw(password, BCrypt.gensalt())
    }
}