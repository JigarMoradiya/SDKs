package com.sdk.network.customs

import android.os.AsyncTask
import com.sdk.BuildConfig
import java.io.*
import java.net.URL


class DownloadCertificateTask(outputFile: File) : AsyncTask<Void?, Void?, File?>() {
    var urlString: String = BuildConfig.CERTIFICATE_URL
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