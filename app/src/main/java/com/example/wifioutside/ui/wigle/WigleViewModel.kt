package com.example.wifioutside.ui.wigle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WigleViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is wigle Fragment"
    }
    val text: LiveData<String> = _text
}