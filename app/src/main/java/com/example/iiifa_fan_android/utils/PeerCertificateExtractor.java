package com.example.iiifa_fan_android.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import static java.util.Base64.getEncoder;

/**
 * Created by fabiomsr on 3/7/16.
 */
public class PeerCertificateExtractor {

    /**
     * Get peer certificate(Public key to sha256 to base64)
     * @param certificate Crt or der or pem file with a valid certificate
     * @return
     */
    public static String extract(File certificate){

        FileInputStream inputStream = null;

        try{
            inputStream = new FileInputStream(certificate);
            X509Certificate x509Certificate = (X509Certificate) CertificateFactory.getInstance("X509")
                    .generateCertificate(inputStream);

            byte[] publicKeyEncoded = x509Certificate.getPublicKey().getEncoded();
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] publicKeySha256 = messageDigest.digest(publicKeyEncoded);
            byte[] publicKeyShaBase64 = new byte[0];
            String publicKeyShaBase64String = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                publicKeyShaBase64 = getEncoder().encode(publicKeySha256);
            } else
                publicKeyShaBase64String = android.util.Base64.encodeToString(publicKeySha256, 0);
            if (publicKeyShaBase64String != null)
                return "sha256/" + publicKeyShaBase64String;
            else
                return "sha256/" + new String(publicKeyShaBase64);
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

}