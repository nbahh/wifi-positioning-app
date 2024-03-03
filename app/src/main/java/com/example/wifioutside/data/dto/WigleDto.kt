package com.example.wifioutside.data.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class WigleDto constructor(
    @JsonProperty("trilat") val trilat: Double,
    @JsonProperty("trilong") val trilong: Double,
    @JsonProperty("ssid") val ssid: String,
    @JsonProperty("qos") val qos: Int,
    @JsonProperty("netid") val netid: String
    )