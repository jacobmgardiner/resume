package com.yoloapps.pitchtrainer

class Tone {
    companion object {
        const val NOTE_C = 0
        const val NOTE_CS = 1
        const val NOTE_D = 2
        const val NOTE_DS = 3
        const val NOTE_E = 4
        const val NOTE_F = 5
        const val NOTE_FS = 6
        const val NOTE_G = 7
        const val NOTE_GS = 8
        const val NOTE_A = 9
        const val NOTE_AS = 10
        const val NOTE_B = 11

        const val FREQ_C0 = 16.35160
        const val FREQ_CS0 = 17.32391
        const val FREQ_D0 = 18.35405
        const val FREQ_DS0 = 19.44544
        const val FREQ_E0 = 20.60172
        const val FREQ_F0 = 21.82676
        const val FREQ_FS0 = 23.12465
        const val FREQ_G0 = 24.49971
        const val FREQ_GS0 = 25.95654
        const val FREQ_A0 = 27.50000
        const val FREQ_AS0 = 29.13524
        const val FREQ_B0 = 30.86771

        const val FREQ_C4 = 261.63
        const val FREQ_CS4 = 277.18
        const val FREQ_D4 = 293.66
        const val FREQ_DS4 = 311.13
        const val FREQ_E4 = 329.63
        const val FREQ_F4 = 349.23
        const val FREQ_FS4 = 369.99
        const val FREQ_G4 = 392.00
        const val FREQ_GS4 = 415.30
        const val FREQ_A4 = 440.00
        const val FREQ_AS4 = 466.16
        const val FREQ_B4 = 493.88

        val NOTE_LABELS = listOf(
            "C",
            "C#",
            "D",
            "D#",
            "E",
            "F",
            "F#",
            "G",
            "G#",
            "A",
            "A#",
            "B",
        )

        val FREQUENCIES = listOf(
            FREQ_C0,
            FREQ_CS0,
            FREQ_D0,
            FREQ_DS0,
            FREQ_E0,
            FREQ_F0,
            FREQ_FS0,
            FREQ_G0,
            FREQ_GS0,
            FREQ_A0,
            FREQ_AS0,
            FREQ_B0
        )

        val FREQUENCIES_ALL = listOf(
            FREQ_C0,
            FREQ_CS0,
            FREQ_D0,
            FREQ_DS0,
            FREQ_E0,
            FREQ_F0,
            FREQ_FS0,
            FREQ_G0,
            FREQ_GS0,
            FREQ_A0,
            FREQ_AS0,
            FREQ_B0,
            FREQ_C0 * 2
        )

        val FREQUENCIES_NO_SHARPS = listOf(
            FREQ_C4,
            FREQ_D4,
            FREQ_E4,
            FREQ_F4,
            FREQ_G4,
            FREQ_A4,
            FREQ_B4
        )

        val FREQUENCIES_SCALE = listOf(
            FREQ_C4,
            FREQ_D4,
            FREQ_E4,
            FREQ_F4,
            FREQ_G4,
            FREQ_A4,
            FREQ_B4,
            FREQ_C4 * 2
        )

        const val DURATION_WHOLE_NOTE = 1000L

        fun getOctave(frequency: Double): Int {
            var n = frequency
            var o = 0
            while (n > 40.355) {
                n /= 2
                o++
            }
            return 0
        }

        fun getNoteLabel(frequency: Double): String {
            var n = frequency
            var o = 0
            while (n > (FREQ_C0 * 2 + FREQ_B0) / 2) {
                n /= 2
                o++
            }
            FREQUENCIES.forEachIndexed { i, f ->
                if (n < (f + FREQUENCIES_ALL[i+1]) / 2)
                    return NOTE_LABELS[i]
            }
            return "?"
        }

        fun getNote(frequency: Double): Int {
            var n = frequency
            var o = 0
            while (n > (FREQ_C0 * 2 + FREQ_B0) / 2) {
                n /= 2
                o++
            }
            FREQUENCIES.forEachIndexed { i, f ->
                if (n < (f + FREQUENCIES_ALL[i+1]) / 2)
                    return i
            }
            return -1
        }

        fun getNoteLabelWithOctave(frequency: Double): String {
            return getNoteLabel(frequency) + getOctave(frequency)
        }

        fun scaleToAll(note: Int): Int {
            return when (note) {
                0 -> 0
                1 -> 2
                2 -> 4
                3 -> 5
                4 -> 7
                5 -> 9
                6 -> 11
                7 -> 0
                else -> -1
            }
        }

        fun notes12to7(note: Int): Int {
            return when (note) {
                0 -> 0
                2 -> 1
                4 -> 2
                5 -> 3
                7 -> 4
                9 -> 5
                11 -> 6
                else -> -1
            }
        }
    }

//    var frequency = FREQ_C4
//        set(value) {
//            field = value
//            if (constr) init()
//        }
//    var duration = 1500
//        set(value) {
//            field = value
//            if (constr) buildAudioManager()
//        }
//    var volume = 1.0
//        set(value) {
//            field = value
//            if (constr) init()
//        }
//
//    private val sampleRate = 8000
//    private var sampleSize = (duration * sampleRate)
//
//    private var audioTrack: AudioTrack? = null
//
//    private var tone: ByteArray? = null
//
//    constructor(note: Int) : this() {
//        this.frequency = FREQUENCIES[note]
//
//        constr = true
//
//        init()
//    }
//
//    //    constructor(context: Context, frequency: Double) : this(context) {
//    constructor(frequency: Double) : this() {
//        this.frequency = frequency
//
//        constr = true
//
//        init()
//    }
//
//    //    constructor(context: Context, note: Int, duration: Int) : this(context) {
//    constructor(note: Int, duration: Int) : this() {
//        this.duration = duration
//        frequency = FREQUENCIES[note]
//
//        constr = true
//
//        init()
//    }
//
//    //    constructor(context: Context, frequency: Double, duration: Int) : this(context) {
//    constructor(frequency: Double, duration: Int) : this() {
//        this.duration = duration
//        this.frequency = frequency
//
//        constr = true
//
//        init()
//    }
//
//    private fun init() {
//        // TODO("thread/coroutine stuffs")
////        val thread = Thread {
////            generateSample()
//        generateTone()
//        buildAudioManager()
////        }
////        thread.start()
//    }
//
//    private fun generateTone() {
//        sampleSize = (/*duration * */sampleRate)
//        tone = ByteArray(sampleSize * 2)
//        var idx = 0
//        for (i in 10 until sampleSize + 10) {
//            val b = (sin(2 * Math.PI * i / (sampleRate / frequency)) * 32767/* * volume*/).toInt()/*.toByte()*//*.toShort()*/
//            tone!![idx++] = (0x00ff and b).toByte()
//            tone!![idx++] = (b and 0xff00 ushr 8).toByte()
//        }
//    }
//
//    private fun saveDataToFile() {
//        //TODO("")
//    }
//
//    private fun buildAudioManager() {
////        if (audioTrack?.state == AudioTrack.STATE_INITIALIZED) return
//        audioTrack = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//            AudioTrack.Builder()
//                .setAudioFormat(
//                    AudioFormat.Builder()
//                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
//                        .setSampleRate(sampleRate)
//                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
//                        .build()
//                )
//                .setBufferSizeInBytes(tone!!.size * duration)
////                .setPerformanceMode(AudioTrack.PERFORMANCE_MODE_LOW_LATENCY)
//                .build()
//        } else {
//            AudioTrack(
//                AudioManager.STREAM_MUSIC,
//                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
//                AudioFormat.ENCODING_PCM_16BIT, tone!!.size * duration,
//                AudioTrack.MODE_STATIC
//            )
//        }
//
//        audioTrack!!.notificationMarkerPosition = sampleSize
//        audioTrack!!.setPlaybackPositionUpdateListener(object : AudioTrack.OnPlaybackPositionUpdateListener {
//            override fun onPeriodicNotification(track: AudioTrack?) {}
//
//            override fun onMarkerReached(track: AudioTrack?) {
//                audioTrack!!.stop()
////                audioTrack!!.release()
//                listeners.forEach {
//                    it()
//                }
//            }
//        })
//    }
//
//    fun play() {
////        if (audioTrack?.state == AudioTrack.STATE_UNINITIALIZED) buildAudioManager()
//        for (i in 0 until duration) {
//            tone?.let { audioTrack?.write(it, 0, it.size) }
//        }
//        audioTrack?.play()
//    }
//
//    fun pause() {
//        audioTrack?.pause()
//    }
//
//    fun stop() {
//        audioTrack?.pause()
//        audioTrack?.flush()
//        audioTrack?.release()
//    }
//
//    fun isPlaying(): Boolean {
//        return audioTrack?.playState?.equals(AudioTrack.PLAYSTATE_PLAYING) ?: false
//    }
//
//    fun addOnPlayEndedListener(listener: () -> Unit) {
//        listeners.add(listener)
//    }
}
