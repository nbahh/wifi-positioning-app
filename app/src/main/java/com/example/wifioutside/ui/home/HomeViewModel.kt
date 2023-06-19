package com.example.wifioutside.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Scanned WiFi networks:"
    }
    val text: LiveData<String> = _text
}