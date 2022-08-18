package com.example.iiifa_fan_android.utils

import android.app.DownloadManager
import android.os.AsyncTask
import android.os.Build
import android.os.Environment
import androidx.core.content.ContextCompat.getSystemService
import java.io.*
import java.net.URL


class DownloadCertificateTask(outputFile: File) : AsyncTask<Void?, Void?, File?>() {
    var urlString: String = Constants.CERT_URL
    var outputFile: File = outputFile
    override fun doInBackground(vararg params: Void?): File? {
        try {
            BufferedInputStream(URL(urlString).openStream()).use { inputStream ->
                FileOutputStream(outputFile).use { fileOS ->
                    val data = ByteArray(1024)
                    var byteContent: Int
                    while (inputStream.read(data, 0, 1024).also { byteContent = it } != -1) {
                        fileOS.write(data, 0, byteContent)
                    }
                    return outputFile
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }


}