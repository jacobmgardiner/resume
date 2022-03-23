package com.yoloapps.pitchtrainer

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ObjCObjectVar
import kotlinx.cinterop.pointed
import kotlinx.cinterop.value
import platform.Foundation.*

actual class MultiplatformFile actual constructor(val parentpath: String, val filename: String) {
    actual val absolutePath: String
        get() = parentpath + filename
    actual val parent: String
        get() = parentpath

    actual val bytes: ByteArray
        get() = TODO()

    private val url by lazy { (NSFileManager.defaultManager().URLsForDirectory(NSDocumentDirectory, NSUserDomainMask).last() as NSURL).URLByAppendingPathComponent(filename)!! }

    private var fileHandle: NSFileHandle? = NSFileHandle.fileHandleForWritingToURL(url, null)

    actual fun createNewFile() {
        //TODO
    }

    actual fun writeText(text: String) {
        val error: CPointer<ObjCObjectVar<NSError?>>? = null
        println("$url")
        println("${(text as NSString).writeToURL(url, true, NSUTF8StringEncoding, error)}")
        println("!!!!!!!!! ERROR: ${error?.pointed?.value?.description}")

        fileHandle = NSFileHandle.fileHandleForWritingToURL(url, null)
    }

    actual fun appendText(text: String) {var error: CPointer<ObjCObjectVar<NSError?>>? = null
        if (fileHandle == null) fileHandle = NSFileHandle.fileHandleForWritingToURL(url, error)
        println("!!!!!!!!! ERROR: ${error?.pointed?.value?.description}")
        fileHandle?.let {
            it.seekToEndOfFile()
            it.writeData((text as NSString).dataUsingEncoding(NSUTF8StringEncoding)!!, error)
//            println("${(text as NSString).writeToURL(url, true, NSUTF8StringEncoding, error)}")
            println("!!!!!!!!! ERROR: ${error?.pointed?.value?.description}")
        }
    }
}