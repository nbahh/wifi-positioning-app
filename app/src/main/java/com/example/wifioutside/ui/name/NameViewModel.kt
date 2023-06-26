package com.example.wifioutside.ui.name

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NameViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Localization by networks name will be here"
    }
    val text: LiveData<String> = _text
}