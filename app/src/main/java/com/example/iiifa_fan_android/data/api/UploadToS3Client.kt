package com.example.iiifa_fan_android.data.api

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UploadToS3Client {
    companion object {
        lateinit var client: OkHttpClient

        @JvmStatic
         fun getCall(
            s3SignedPutUrl: String, file_extension: String,
            file_type: String, image_file: File
        ): okhttp3.Call {
            client = OkHttpClient.Builder().build()

            val mediaType = "$file_type/$file_extension".toMediaType()

            val body: RequestBody = image_file.asRequestBody(mediaType)
            val request: Request = Request.Builder()
                .url(s3SignedPutUrl)
                .method("PUT", body)
                .addHeader("Content-Type", "$file_type/$file_extension")
                .build()

            return client.newCall(request)
        }
    }
}

