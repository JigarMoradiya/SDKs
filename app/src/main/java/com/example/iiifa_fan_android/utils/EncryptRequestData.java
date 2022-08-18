package com.example.iiifa_fan_android.utils;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.example.iiifa_fan_android.BuildConfig;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class EncryptRequestData {
    private static com.example.iiifa_fan_android.utils.EncryptRequestData encryptRequestData;
    private static com.example.iiifa_fan_android.utils.PrefManager prefManager;
    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static com.example.iiifa_fan_android.utils.EncryptRequestData getInstance() {
        if (encryptRequestData == null) {
            synchronized (com.example.iiifa_fan_android.utils.EncryptRequestData.class) {
                if (encryptRequestData == null) {
                    encryptRequestData = new com.example.iiifa_fan_android.utils.EncryptRequestData();
                    prefManager = new com.example.iiifa_fan_android.utils.PrefManager(MyApplication.Companion.getInstance());
                }
            }
        }
        return encryptRequestData;
    }

    //Encrypt the give string using specif key
    public static String getEncryptedData(String jsonObjectString) {
        try {
            Log.d("encryption_original", jsonObjectString);
            getInstance();

            String key = CustomFunctions.getKeyThree();

            if (prefManager != null && prefManager.getToken() != null && !TextUtils.isEmpty(prefManager.getToken()))
                key = prefManager.getToken();


            Log.d("encryption_key", key);

            byte[] keySkec = key.getBytes();
            SecretKeySpec skc = new SecretKeySpec(keySkec, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skc);
            return toBase64(cipher.doFinal(jsonObjectString.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            Log.e("EXception: ", e.getLocalizedMessage());
            return null;
        }
    }


    //Encrypt the give string using specif key
//    public static String getEncryptedDataForChat(String jsonObjectString) {
//        try {
//            getInstance();
//
//            String key;
//
//            if (BuildConfig.FLAVOR.contains("prod")) {
//                key =com.example.iiifa_fan_android.utils.CustomClasses.getChatProd();
//            } else {
//                key =com.example.iiifa_fan_android.utils.CustomClasses.getChatOther();
//            }
//
//
//            Log.d("encryption_token", key);
//
//            byte[] keySkec = key.getBytes();
//            SecretKeySpec skc = new SecretKeySpec(keySkec, "AES");
//            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//            cipher.init(Cipher.ENCRYPT_MODE, skc);
//            Log.i("encryption_key_to_send", toBase64(cipher.doFinal(jsonObjectString.getBytes(StandardCharsets.UTF_8))));
//            return toBase64(cipher.doFinal(jsonObjectString.getBytes(StandardCharsets.UTF_8)));
//        } catch (Exception e) {
//            Log.e("EXception: ", e.getLocalizedMessage());
//            return null;
//        }
//    }


    //Encrypt the give string using specif key
    public static String getDecryptedData(String strToDecrypt) {
        try {

            String key = CustomFunctions.getKeyThree();
            if (prefManager != null && prefManager.getToken() != null && !TextUtils.isEmpty(prefManager.getToken()))
                key = prefManager.getToken();

            Log.d("decryption_key", key);

            byte[] keySkec = key.getBytes();
            SecretKeySpec skc = new SecretKeySpec(keySkec, "AES");
            Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, skc);
            byte[] decryptValue = Base64.decode(strToDecrypt, Base64.NO_WRAP);
            byte[] decValue = c.doFinal(decryptValue);
            String decryptedValue = new String(decValue);
            Log.d("decrypted_string", decryptedValue);
            return decryptedValue;
        } catch (Exception e) {
            Log.e("EXception: ", e.getLocalizedMessage());
            return decryptByDefaultToken(strToDecrypt);
        }
    }

    //decrypy using default token
    public static String decryptByDefaultToken(String strToDecrypt) {
        try {

            String key = CustomFunctions.getKeyThree();

            Log.d("decryption_key", key);

            byte[] keySkec = key.getBytes();
            SecretKeySpec skc = new SecretKeySpec(keySkec, "AES");
            Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, skc);
            byte[] decryptValue = Base64.decode(strToDecrypt, Base64.NO_WRAP);
            byte[] decValue = c.doFinal(decryptValue);
            String decryptedValue = new String(decValue);
            Log.d("decrypted_string", decryptedValue);
            return decryptedValue;
        } catch (Exception e) {
            Log.e("EXception: ", e.getLocalizedMessage());
            CustomClasses.handleForbiddenResponse();
            return null;
        }
    }

    private static String toBase64(byte[] bytes) {
        Log.i("encrypted_string", Base64.encodeToString(bytes, Base64.NO_WRAP));
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

    //Encryption for Shared Preferences Data
    public static String encrypt(String jsonObjectString) {
        try {

            Log.d("encryption_original", jsonObjectString);
            String key = CustomFunctions.getKeyThree();
            Log.d("encryption_key", key);

            byte[] keySkec = key.getBytes();
            SecretKeySpec skc = new SecretKeySpec(keySkec, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skc);
            return toBase64(cipher.doFinal(jsonObjectString.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            Log.e("EXception: ", e.getLocalizedMessage());
            return null;
        }
    }

    //Decryption for Shared Preferences Data
    public static String decrypt(String strToDecrypt) {
        try {

            String key = CustomFunctions.getKeyThree();
            Log.d("decryption_key", key);

            byte[] keySkec = key.getBytes();
            SecretKeySpec skc = new SecretKeySpec(keySkec, "AES");
            Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, skc);
            byte[] decryptValue = Base64.decode(strToDecrypt, Base64.NO_WRAP);
            byte[] decValue = c.doFinal(decryptValue);
            String decryptedValue = new String(decValue);
            Log.d("decrypted_string", decryptedValue);
            return decryptedValue;
        } catch (Exception e) {
            Log.e("EXception: ", e.getLocalizedMessage());
            return decryptByDefaultToken(strToDecrypt);
        }
    }


    public static String getDecryptedDataForChat(String strToDecrypt) {
        try {
            String key;

            if (BuildConfig.FLAVOR.contains("prod")) {
                key = CustomFunctions.getChatProd();
            } else if (BuildConfig.FLAVOR.contains("uat")) {
                key = CustomFunctions.getChatUAT();
            } else if (BuildConfig.FLAVOR.contains("dev")) {
                key = CustomFunctions.getChatDevOther();
            } else {
                key = CustomFunctions.getChatTestOther();
            }

            Log.d("decryption_key", key);

            byte[] keySkec = key.getBytes();
            SecretKeySpec skc = new SecretKeySpec(keySkec, "AES");
            Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, skc);
            byte[] decryptValue = Base64.decode(strToDecrypt, Base64.NO_WRAP);
            byte[] decValue = c.doFinal(decryptValue);
            String decryptedValue = new String(decValue);
            Log.d("decrypted_string", decryptedValue);
            return decryptedValue;
        } catch (Exception e) {
            return strToDecrypt;
        }
    }


    //Encrypt the give string using specif key
    public static String getEncryptedDataForChat(String jsonObjectString) {
        try {
            getInstance();

            String key;

            if (BuildConfig.FLAVOR.contains("prod")) {
                key = CustomFunctions.getChatProd();
            } else if (BuildConfig.FLAVOR.contains("uat")) {
                key = CustomFunctions.getChatUAT();
            } else if (BuildConfig.FLAVOR.contains("dev")) {
                key = CustomFunctions.getChatDevOther();
            } else {
                key = CustomFunctions.getChatTestOther();
            }


            Log.d("encryption_token", key);

            byte[] keySkec = key.getBytes();
            SecretKeySpec skc = new SecretKeySpec(keySkec, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skc);
            Log.i("encryption_key_to_send", toBase64(cipher.doFinal(jsonObjectString.getBytes(StandardCharsets.UTF_8))));
            return toBase64(cipher.doFinal(jsonObjectString.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            Log.e("EXception: ", e.getLocalizedMessage());
            return null;
        }
    }
}
