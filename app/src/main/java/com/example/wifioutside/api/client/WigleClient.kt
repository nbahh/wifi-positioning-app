package com.example.wifioutside.api.client

import android.util.Log
import com.example.wifioutside.data.dto.WigleDto

class WigleClient {
    private val retrofit = RetrofitClient.getClient();
    private val wigleApi = retrofit.create(WigleApi::class.java)

    fun getNetworkByBSSID(bssid: String) : WigleDto? {
        val wigleResponse = wigleApi.getNetwork(bssid)
            .execute()
        if (!wigleResponse.isSuccessful) {
            Log.d("WARN", "[${this.javaClass.simpleName}] Unable to get network by BSSID, " +
                    "error code ${wigleResponse.code()}, message ${wigleResponse.message()}")
            return null;
        }

        if (wigleResponse.body()?.results?.size == 0) {
            Log.d("WARN", "[${this.javaClass.simpleName}] Unable to get network by BSSID, " +
                    "this network not found.")
            return null;
        }
        return wigleResponse.body()?.results?.get(0);
    }

}