package com.example.wifioutside

import com.example.wifioutside.api.client.WigleClient
import com.example.wifioutside.data.dto.WigleDto
import org.junit.Test

class WigleClientTest {

    private val wigleClient: WigleClient = WigleClient()

    @Test
    fun requestWorks() {
        val wigle = wigleClient.getNetworkByBSSID("B6:CA:B5:DD:EA:C0")
        println()
    }
}