package com.example.wifioutside.api.client

import com.example.wifioutside.api.interceptor.RequestInterceptor
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.time.Duration

object RetrofitClient {
    private const val WIGLE_URL = "https://api.wigle.net/api/v2/"

    val okHttpClient = OkHttpClient()
        .newBuilder()
        .connectTimeout(Duration.ofSeconds(30))
        .addInterceptor(RequestInterceptor)
        .build()

    fun getClient(): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(WIGLE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
}