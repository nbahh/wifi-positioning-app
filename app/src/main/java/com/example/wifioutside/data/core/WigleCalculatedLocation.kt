package com.example.wifioutside.data.core

import com.example.wifioutside.data.dto.WigleDto

class WigleCalculatedLocation(
    val trilat: Double,
    val trilong: Double,
    val ssid: String,
    val qos: Int,
    val netid: String
) {
    constructor(dto: WigleDto) : this(dto.trilat, dto.trilong, dto.ssid, dto.qos, dto.netid)

    override fun toString(): String {
        return "WigleCalculatedLocation(trilat=$trilat, trilong=$trilong, ssid='$ssid', qos=$qos, netid='$netid')"
    }

}