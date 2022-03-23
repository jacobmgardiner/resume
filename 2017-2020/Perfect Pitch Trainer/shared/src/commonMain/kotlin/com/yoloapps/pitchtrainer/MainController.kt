package com.yoloapps.pitchtrainer

//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlin.random.Random

class MainController() {
//    private val _note = MutableStateFlow(getNextNote())
//    val note: StateFlow<NoteUtils.NoteLetter> = _note.asStateFlow()
    val note = ObservableData<NoteUtils.NoteLetter>(getNextNote())
    fun observeNote(callback: (NoteUtils.NoteLetter) -> Unit) {
        println("[MAIN CONTROLLER] SETTING NOTE ON UPDATE")
        note.onUpdate = callback
    }

//    private val _succeeded = MutableStateFlow(false)
//    val succeeded: StateFlow<Boolean> = _succeeded.asStateFlow()
    val succeeded = ObservableData(false)
    fun observeSucceeded(callback: (Boolean) -> Unit) {
        println("[MAIN CONTROLLER] SETTING SUCCEEDED ON UPDATE")
        succeeded.onUpdate = callback
    }

    private val _presentationVisible = MutableStateFlow(PresentationVisibility(false))
    val presentationVisible: StateFlow<PresentationVisibility> = _presentationVisible
//private val _presentationVisible = MutableLiveData(false)
//    val presentationVisible: LiveData<Boolean> = _presentationVisible
    @OptIn(InternalCoroutinesApi::class)
    suspend fun observePresentationVisible(callback: (Boolean) -> Unit) {
        presentationVisible.collect {
            callback(it.visible)
        }
    }

    var inputAllowed: Boolean = false

    private var lastAttempt: Attempt? = null

    private fun getNextNote(): NoteUtils.NoteLetter {
        return if((lastAttempt?.result ?: 1f) < 1f)
//            _note.value
            note.value
        else
            randomNote()
    }

    private fun randomNote(): NoteUtils.NoteLetter {
        return NoteUtils.NoteLetter.values()[Random.nextInt(NoteUtils.NoteLetter.values().size)]
    }

    fun onResponse(response: Int, audioController: AudioController) {
        inputAllowed = false

        audioController.setAudioEventListener(object : AudioManager.AudioEventListener {
            override fun onStart() {
            }
            override fun onPause() {
            }
            override fun onCancel() {
            }
            override fun onComplete() {
                println("XXX [MAIN CONTROLLER] USER CHOSE NOTE: ${response}")
//                lastAttempt = Attempt.create(response, _note.value)
                lastAttempt = Attempt.create(response, note.value)
//                _succeeded.value = lastAttempt!!.success()
                succeeded.value = lastAttempt!!.success()

                println("XXX changing visible to true")
                _presentationVisible.value = PresentationVisibility(true)
            }
            override fun onEnd() {
//                TODO("Not yet implemented")
            }
        })

        audioController.play()
    }

    fun onNotePresentationComplete() {
        println("XXX changing visible to false")
        _presentationVisible.value = PresentationVisibility(false)
        inputAllowed = true
    }

    fun onResultComplete() {
        println("XXX changing visible to false")
        _presentationVisible.value = PresentationVisibility(false)

        //TODO("call after presentation anim update")

        println("XXX [MAIN CONTROLLER] FINISHED SHOWING RESULT. CHOOSING NEW NOTE")
//        _note.value = getNextNote()
        note.value = getNextNote()
        println("XXX [MAIN CONTROLLER] NEW NOTE: ${note.value}")

        println("XXX changing visible to true")
        _presentationVisible.value = PresentationVisibility(true)
    }

    fun start() {
        println("XXX FRAMEWORK VERSION: 0")

//        _note.value = getNextNote()
        note.value = getNextNote()
        println("XXX [MAIN CONTROLLER] STARTING WITH NOTE: ${note.value}")

        println("XXX changing visible to true")
        _presentationVisible.value = PresentationVisibility(true)
    }
}