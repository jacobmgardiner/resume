package com.yoloapps.reactiontimetracker

import kotlinx.cinterop.*
import platform.Foundation.*

actual class KMMFile actual constructor(val parentpath: String, val filename: String) {
    actual val absolutePath: String
        get() = parentpath + filename
    actual val parent: String
        get() = parentpath

    private val url by lazy { (NSFileManager.defaultManager().URLsForDirectory(NSDocumentDirectory, NSUserDomainMask).last() as NSURL).URLByAppendingPathComponent(filename)!! }

    private var fileHandle: NSFileHandle? = NSFileHandle.fileHandleForWritingToURL(url, null)

    actual fun createNewFile() {
        //TODO
    }

    actual fun writeText(text: String) {
//        val url = (NSFileManager.defaultManager().URLsForDirectory(NSDocumentDirectory, NSUserDomainMask).last() as NSURL).URLByAppendingPathComponent(filename)!!
        var error: CPointer<ObjCObjectVar<NSError?>>? = null
//        println("${str.writeToURL(NSURL(fileURLWithPath = absolutePath), true, NSUTF8StringEncoding, error)}")
        println("$url")
        println("${(text as NSString).writeToURL(url, true, NSUTF8StringEncoding, error)}")
        println("!!!!!!!!! ERROR: ${error?.pointed?.value?.description}")
//        println("written")

        fileHandle = NSFileHandle.fileHandleForWritingToURL(url, null)
    }

    actual fun appendText(text: String) {
//        NSFileHandle *myHandle = [NSFileHandle fileHandleForWritingAtPath:documentTXTPath];
//        [myHandle seekToEndOfFile];
//        [myHandle writeData:[savedString dataUsingEncoding:NSUTF8StringEncoding]];


        var error: CPointer<ObjCObjectVar<NSError?>>? = null
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