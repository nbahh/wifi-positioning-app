package com.example.wifioutside.data.core

class WifiScanResult (
    val SSID: String,
    val BSSID: String,
    val frequency: Int,
    val level: Int
) {
    override fun toString(): String {
        return "$SSID (${frequencyToBand()})   $level dBm"
    }

    private fun frequencyToBand() : String {
        if (frequency in 2400..2500) {
            return "2.4GHz"
        }
        if (frequency in 5180..5875) {
            return "5GHz"
        }
        if (frequency in 5945..7125) {
            return "6GHz"
        }
        return (frequency.toString() + "hz")
    }
}