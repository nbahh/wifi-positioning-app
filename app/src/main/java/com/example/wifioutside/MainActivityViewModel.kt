package com.example.wifioutside

import android.net.wifi.ScanResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wifioutside.data.core.GpsLocation
import com.example.wifioutside.data.core.WifiScanResult
import com.example.wifioutside.data.core.WigleCalculatedLocation

class MainActivityViewModel : ViewModel() {
    private val _wifiList = MutableLiveData<List<WifiScanResult>>()
    private val _wigleEstimation = MutableLiveData<WigleCalculatedLocation>()
    private val _gpsLocation = MutableLiveData<GpsLocation>()
    private val _manualLocation = MutableLiveData<GpsLocation>()


    val wifiList: LiveData<List<WifiScanResult>>
        get() = _wifiList

    val gpsLocation: LiveData<GpsLocation>
        get() = _gpsLocation

    val manualLocation: LiveData<GpsLocation>
        get() = _manualLocation


    fun updateGpsLocation(gpsLocation: GpsLocation) {
        _gpsLocation.value = gpsLocation
    }

    fun updateWifiList(wifis: MutableList<WifiScanResult>) {
        _wifiList.value = wifis
    }

    fun updateManualLocation(manualLocation: GpsLocation) {
        _manualLocation.value = manualLocation
    }


    fun getStrongestWifiScanResult() : WifiScanResult? {
        return _wifiList.value?.maxWith(Comparator.comparing { it.level })
    }

    val wigleEstimation: LiveData<WigleCalculatedLocation>
        get() = _wigleEstimation

    fun updateWigleEstimation(estimation:  WigleCalculatedLocation) {
        _wigleEstimation.value = estimation
    }
}