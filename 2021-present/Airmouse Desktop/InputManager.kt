import androidx.compose.ui.input.key.Key
import java.awt.Robot
import java.awt.Toolkit
import java.awt.event.InputEvent
import java.awt.event.KeyEvent

class InputManager {
    var lastInput: InputUpdate? = null
    val robot by lazy { Robot() }
    val dimension by lazy { Toolkit.getDefaultToolkit().screenSize }
    private var paused = false

    fun onUpdate(input: InputUpdate) {
        if (paused) return
        if (lastInput == input) return

        robot.mouseMove((input.x*dimension.width).toInt(), (input.y*dimension.height).toInt())

        if (lastInput?.button0 != input.button0) {
            if (input.button0 == 1) robot.mousePress(InputEvent.BUTTON1_DOWN_MASK)
            else robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK)
        }
        if (lastInput?.button1 != input.button1) {
            if (input.button1 == 1) robot.mousePress(InputEvent.BUTTON2_DOWN_MASK)
            else robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK)
        }
        if (lastInput?.button2 != input.button2) {
            if (input.button2 == 1) robot.mousePress(InputEvent.BUTTON3_DOWN_MASK)
            else robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK)
        }

        //TODO("support caps- maybe separate shift button?")
        input.inputString.chars().forEach {
            val keyCode = KeyEvent.getExtendedKeyCodeForChar(it)
            if (it.isCap()) {
                robot.keyPress(KeyEvent.VK_SHIFT)
                robot.keyPress(keyCode)
                robot.keyRelease(keyCode)
                robot.keyRelease(KeyEvent.VK_SHIFT)
            } else {
                try {
                    robot.keyPress(keyCode)
                    robot.keyRelease(keyCode)
                } catch (e: java.lang.IllegalArgumentException) {
                    e.printStackTrace()
                    println("ERROR! Key $keyCode not supported!")
                }
            }
        }

        if (input.backspace == 1) {
            robot.keyPress(KeyEvent.VK_BACK_SPACE)
            robot.keyRelease(KeyEvent.VK_BACK_SPACE)
        }
        if (input.enter == 1) {
            robot.keyPress(KeyEvent.VK_ENTER)
            robot.keyRelease(KeyEvent.VK_ENTER)
        }

//        TODO("implement volume")
//        TODO("implement app launch")
        if (input.appLaunch >= 0) SystemTaskHandler.runApp(Main.appListPaths[input.appLaunch])

        lastInput = input
    }

    fun togglePause(): Boolean {
        paused = !paused
        return paused
    }
}

private fun Int.isCap(): Boolean {
    return this in 65..90
}
