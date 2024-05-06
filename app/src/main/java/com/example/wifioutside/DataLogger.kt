package com.example.wifioutside

import android.net.wifi.ScanResult
import android.os.Environment
import android.util.Log
import com.example.wifioutside.data.core.WifiScanResult
import java.io.File
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DataLogger {
    fun logWifiScan(scanResults: List<ScanResult>) {
        val appDirectory = createDirectory("wifi_scan")
        if (appDirectory != null) {
            Log.d("DEBUG", "app directory created")
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val current = LocalDateTime.now().format(formatter)
            val file = File(appDirectory, "$current.txt")
            try {
                if (!file.exists()) {
                    val fileCreated = file.createNewFile()
                    if (fileCreated) {
                        val builder: StringBuilder = StringBuilder()
                        scanResults.stream().forEach { result ->
                            builder.append(result).append("\n")
                        }
                        file.writeText(builder.toString())
                    }
                }
            } catch (e: IOException) {

            }
        }

    }

    private fun createDirectory(directory: String): File? {
        val documentDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val appDirectory = File(documentDirectory, "positioning_logs/$directory")

        if (!appDirectory.exists()) {
            val directoryCreated = appDirectory.mkdir()
            if (!directoryCreated) {
                return null
            }
        }
        return appDirectory
    }
}