package com.yoloapps.airmouse

import com.google.gson.Gson
import com.google.gson.JsonParseException
import java.io.PrintWriter
import java.util.*
import kotlin.reflect.KClass

object SocketUtils {
    val gson by lazy { Gson() }

    fun sendObject(writer: PrintWriter, data: Any) {
        writer.println(gson.toJson(data))
        writer.flush()
    }

    fun <T : Any> getNextObject(scanner: Scanner, type: KClass<T>): Any? {
        return if (scanner.hasNextLine()) {
            try {
                gson.fromJson(scanner.nextLine(), type.java)
            } catch (e: JsonParseException) {
                e.printStackTrace()
                return null
            }
        } else null
    }
}