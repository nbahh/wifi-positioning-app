package com.example.wifioutside

import android.net.wifi.ScanResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    private val _wifiList = MutableLiveData<MutableList<ScanResult>>()

    init {
        _wifiList.value = ArrayList()
    }

    val wifiList = _wifiList

    fun updateWifiList(wifis: MutableList<ScanResult>) {
        wifiList.value = wifis
    }
}