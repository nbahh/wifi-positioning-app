package com.example.wifioutside.ui.wigle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WigleViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "WiGLE stuff here"
    }
    val text: LiveData<String> = _text
}