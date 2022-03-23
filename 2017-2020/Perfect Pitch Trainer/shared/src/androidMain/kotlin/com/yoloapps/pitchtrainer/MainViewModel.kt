package com.yoloapps.pitchtrainer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    val controller = MainController()

//    private val _succeeded = MutableLiveData(false)
//    val succeeded: LiveData<Boolean> = _succeeded
//
//    private val _presentationResId = MutableLiveData(R.drawable.ic_launcher_foreground)
//    val presentationResId: LiveData<Int> = _presentationResId
//
//    private val _note = MutableLiveData(R.drawable.ic_launcher_foreground)
//    val note: LiveData<Int> = _note
//
//    fun test() {
//        _succeeded.value = true
//    }
}