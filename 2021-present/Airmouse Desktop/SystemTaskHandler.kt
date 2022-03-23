object SystemTaskHandler {
    const val MAX_VOLUME_WINDOWS = 65535
    const val MAX_VOLUME_MAC = 7f

    /*fun setVolume(volume: Float) {
        if (SystemUtils.IS_OS_WINDOWS) setVolumeWindows(volume)
        else if(SystemUtils.IS_OS_MAC_OSX) setVolumeMac(volume)
        else if(SystemUtils.IS_OS_LINUX) setVolumeMac(volume)
    }

    private fun setVolumeWindows(volume: Float) {
        val rt = Runtime.getRuntime()
        try {
            val p = rt.exec(pathToNircmdexe.toString() + " setsysvolume ${(MAX_VOLUME_WINDOWS * volume).toString()}")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun setVolumeMac(volume: Float) {
        val command = "set volume ${volume * MAX_VOLUME_MAC}"
        try {
            val pb = ProcessBuilder("osascript", "-e", command)
            pb.directory(File("/usr/bin"))
            val output = StringBuffer()
            val p = pb.start()
            p.waitFor()
            val reader = BufferedReader(InputStreamReader(p.inputStream))
            var line: String
            while (reader.readLine().also { line = it } != null) {
                output.appendLine(line)
            }
            println(output)
        } catch (e: Exception) {
            println(e)
        }
    }*/

    fun runApp(app: String) {
        val process = ProcessBuilder(app).start()
    }
}