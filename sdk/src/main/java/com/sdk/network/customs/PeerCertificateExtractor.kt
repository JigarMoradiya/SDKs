package com.sdk.network.customs

import android.os.Build
import android.util.Base64
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.security.MessageDigest
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate


object PeerCertificateExtractor {

    fun extract(certificate: File?): String {
        var inputStream: FileInputStream? = null
        try {
            inputStream = FileInputStream(certificate)
            val x509Certificate = CertificateFactory.getInstance("X509")
                .generateCertificate(inputStream) as X509Certificate
            val publicKeyEncoded = x509Certificate.publicKey.encoded
            val messageDigest = MessageDigest.getInstance("SHA-256")
            val publicKeySha256 = messageDigest.digest(publicKeyEncoded)
            var publicKeyShaBase64: ByteArray? = ByteArray(0)
            var publicKeyShaBase64String: String? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                publicKeyShaBase64 = java.util.Base64.getEncoder().encode(publicKeySha256)
            } else publicKeyShaBase64String = Base64.encodeToString(publicKeySha256, 0)
            return if (publicKeyShaBase64String != null) "sha256/$publicKeyShaBase64String" else "sha256/" + String(
                publicKeyShaBase64!!
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return ""
    }
}