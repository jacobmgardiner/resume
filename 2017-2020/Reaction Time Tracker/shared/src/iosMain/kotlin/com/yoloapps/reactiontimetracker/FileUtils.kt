package com.yoloapps.reactiontimetracker

import platform.Foundation.*
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIViewController

actual class FileUtils/*(val uiViewController: UIViewController)*/ {
    actual val dataFileSaveLocation: String
    //file:///Users/jacobgardiner/Library/Developer/CoreSimulator/Devices/0773550D-089F-4D31-8E28-F892DBA7DFA0/data/Containers/Data/Application/1F895737-E78A-443C-9577-CCF428ADBAEF/Documents/
//        get() = (NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true).last() as NSURL).absoluteString ?: ""
        get() = (NSFileManager.defaultManager().URLsForDirectory(NSDocumentDirectory, NSUserDomainMask).last() as NSURL).absoluteString ?: ""

    actual fun sendFile(filepath: String/*, uiViewController: Any?*/) {
        println("starting send: $filepath...")
        println("$dataFileSaveLocation")
        val activityVC = UIActivityViewController(
            listOf(NSURL(fileURLWithPath = filepath)),
            null
        )
        println("made list of stuff")
        currentTopViewController().presentViewController(activityVC, true, null)
    }

    private fun currentTopViewController(): UIViewController {
        println("searching for top view...")
//        var topVC: UIViewController? = UIApplication.sharedApplication.delegate?.window?.rootViewController
        var topVC: UIViewController? = UIApplication.sharedApplication.keyWindow?.rootViewController
        println("reference to root view controller: $topVC")
        while ((topVC?.presentedViewController) != null) {
            topVC = topVC.presentedViewController
        }
        println("controller: $topVC")
        return topVC!!
    }
}
