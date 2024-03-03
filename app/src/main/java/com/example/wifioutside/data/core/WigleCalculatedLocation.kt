package com.example.wifioutside.data.core

import com.example.wifioutside.data.dto.WigleDto
import com.fasterxml.jackson.annotation.JsonProperty

class WigleCalculatedLocation(
    val trilat: Double,
    val trilong: Double,
    val ssid: String,
    val qos: Int,
    val netid: String
) {
    constructor(dto: WigleDto) : this(dto.trilat, dto.trilong, dto.ssid, dto.qos, dto.netid)

}