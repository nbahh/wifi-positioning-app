package com.example.wifioutside.data.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class WigleResponseDto(
    @JsonProperty("results") val results: List<WigleDto>)