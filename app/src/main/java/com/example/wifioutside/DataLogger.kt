package com.example.wifioutside

import android.content.Context
import android.net.wifi.ScanResult
import android.os.Environment
import android.widget.Toast
import com.example.wifioutside.data.core.GpsLocation
import com.example.wifioutside.data.core.WigleCalculatedLocation
import java.io.File
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DataLogger {
    fun logWifiScan(context: Context, scanResults: List<ScanResult>) {
        val appDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

        val builder: StringBuilder = StringBuilder()
        scanResults.stream().forEach { result ->
            builder.append(result).append("\n")
        }

        val file = createFile("${appDirectory}/wifiScan",  builder.toString())

        Toast.makeText(context, "Logged to ${file.absolutePath}", Toast.LENGTH_SHORT).show()
    }

    fun logWigle(context: Context, ssid: String, wigleResult: WigleCalculatedLocation?) {
        val appDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

        val body = "SSID used: $ssid, -----> $wigleResult"

        val file = createFile("${appDirectory}/wigle", body)
        Toast.makeText(context, "Logged to ${file.absolutePath}", Toast.LENGTH_SHORT).show()
    }

    fun logGpsWigleAccuracy(context: Context, gps: GpsLocation, wigleResult: WigleCalculatedLocation,
        difference: Float) {
        val appDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

        val body = "Difference: $difference meters, gps: $gps ------ wigle: $wigleResult"

        val file = createFile("${appDirectory}/gps", body)
        Toast.makeText(context, "Logged to ${file.absolutePath}", Toast.LENGTH_SHORT).show()
    }

    fun logGpsManualAccuracy(context: Context, gps: GpsLocation, manual: GpsLocation,
                            difference: Float) {
        val appDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

        val body = "Difference: $difference meters, gps: $gps ------ manual: $manual"

        val file = createFile("${appDirectory}/manual", body)
        Toast.makeText(context, "Logged to ${file.absolutePath}", Toast.LENGTH_SHORT).show()
    }

    fun logWigleManualAccuracy(context: Context, wigle: WigleCalculatedLocation, manual: GpsLocation,
                            difference: Float) {
        val appDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)

        val body = "Difference: $difference meters, wigle: $wigle ------ manual: $manual"

        val file = createFile("${appDirectory}/manual", body)
        Toast.makeText(context, "Logged to ${file.absolutePath}", Toast.LENGTH_SHORT).show()
    }

    private fun createFile(directory: String, body : String) : File {
        val fileDirectory = File(directory)

        if (!fileDirectory.exists()) {
            fileDirectory.mkdir()
        }

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val current = LocalDateTime.now().format(formatter)

        val file = File(directory, "${current}.txt" )
        file.createNewFile()
        val fos = FileOutputStream(file, false)

        fos.write(body.toByteArray(StandardCharsets.UTF_8))
        fos.close()
        return file
    }

}