package com.example.wifioutside.api.client

import com.example.wifioutside.data.dto.WigleResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface WigleApi {
    @GET("network/search")
    @Headers("Authorization: Basic QUlEYzNjMzk4NGRhZTYyNjJlZWMwMWQ5MjM3ZDg4M2JjYzk6YWNlMTJjOTFhNjE5NDcxNWMwM2IxYWZiNGQ3MmNkOWE=")
    fun getNetwork(@Query("netid") netid: String): Call<WigleResponseDto>
}