package com.example.wifioutside

import android.net.wifi.ScanResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wifioutside.data.core.WigleCalculatedLocation

class MainActivityViewModel : ViewModel() {
    private val _wifiList = MutableLiveData<List<ScanResult>>()
    private val _wigleEstimation = MutableLiveData<WigleCalculatedLocation>()


    val wifiList: LiveData<List<ScanResult>>
        get() = _wifiList

    fun updateWifiList(wifis: MutableList<ScanResult>) {
        _wifiList.value = wifis
    }

    fun getStrongestWifiScanResult() : ScanResult? {
        return _wifiList.value?.maxWith(Comparator.comparing { it.level })
    }

    val wigleEstimation: LiveData<WigleCalculatedLocation>
        get() = _wigleEstimation

    fun updateWigleEstimation(estimation:  WigleCalculatedLocation) {
        _wigleEstimation.value = estimation
    }
}