package com.example.wifioutside.data.core

class GpsLocation (
    val latitude: Double,
    val longitude: Double


) {
    override fun toString(): String {
        return "GpsLocation(latitude=$latitude, longitude=$longitude)"
    }
}