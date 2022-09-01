package com.example.iiifa_fan_android.utils

import android.text.TextUtils
import android.util.Base64
import android.util.Log
import com.example.iiifa_fan_android.BuildConfig
import com.example.iiifa_fan_android.data.pref.AppPreferencesHelper
import com.example.iiifa_fan_android.data.pref.PreferencesHelper
import com.example.iiifa_fan_android.utils.CustomClasses.handleForbiddenResponse
import com.example.iiifa_fan_android.utils.CustomFunctions.getChatDevOther
import com.example.iiifa_fan_android.utils.CustomFunctions.getChatProd
import com.example.iiifa_fan_android.utils.CustomFunctions.getChatTestOther
import com.example.iiifa_fan_android.utils.CustomFunctions.getChatUAT
import com.example.iiifa_fan_android.utils.CustomFunctions.keyThree
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


object EncryptRequestData {
    private var encryptRequestData: EncryptRequestData? = null
    private var prefManager : AppPreferencesHelper? = null
    val instance: EncryptRequestData?
        get() {
            if (encryptRequestData == null) {
                synchronized(EncryptRequestData::class.java) {
                    if (encryptRequestData == null) {
                        encryptRequestData = EncryptRequestData
                        prefManager = AppPreferencesHelper(MyApplication.getInstance(), Constants.PREF_NAME)
                    }
                }
            }
            return encryptRequestData
        }

    //Encrypt the give string using specif key
    fun getEncryptedData(jsonObjectString: String): String? {
        return try {
            Log.e("encryption_original", jsonObjectString)
            instance
            var key = keyThree
            if (prefManager?.getToken() != null && !TextUtils.isEmpty(prefManager?.getToken())
            ) key = prefManager?.getToken()?:""
            Log.e("encryption_key", key)
            val keySkec = key.toByteArray()
            val skc = SecretKeySpec(keySkec, "AES")
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, skc)
            toBase64(cipher.doFinal(jsonObjectString.toByteArray(StandardCharsets.UTF_8)))
        } catch (e: Exception) {
            Log.e("EXception: ", e.localizedMessage)
            null
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
    //            Log.e("encryption_token", key);
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
    fun getDecryptedData(strToDecrypt: String?): String? {
        return try {
            var key = keyThree
            if (prefManager?.getToken() != null && !TextUtils.isEmpty(prefManager?.getToken())) key = prefManager?.getToken()?:""
            Log.e("decryption_key", key)
            val keySkec = key.toByteArray()
            val skc = SecretKeySpec(keySkec, "AES")
            val c = Cipher.getInstance("AES/ECB/PKCS5Padding")
            c.init(Cipher.DECRYPT_MODE, skc)
            val decryptValue = Base64.decode(strToDecrypt, Base64.NO_WRAP)
            val decValue = c.doFinal(decryptValue)
            val decryptedValue = String(decValue)
            Log.e("decrypted_string", decryptedValue)
            decryptedValue
        } catch (e: Exception) {
            Log.e("EXception: ", e.localizedMessage)
            decryptByDefaultToken(strToDecrypt)
        }
    }

    //decrypy using default token
    fun decryptByDefaultToken(strToDecrypt: String?): String? {
        return try {
            val key = keyThree
            Log.e("decryption_key", key)
            val keySkec = key.toByteArray()
            val skc = SecretKeySpec(keySkec, "AES")
            val c = Cipher.getInstance("AES/ECB/PKCS5Padding")
            c.init(Cipher.DECRYPT_MODE, skc)
            val decryptValue = Base64.decode(strToDecrypt, Base64.NO_WRAP)
            val decValue = c.doFinal(decryptValue)
            val decryptedValue = String(decValue)
            Log.e("decrypted_string", decryptedValue)
            decryptedValue
        } catch (e: Exception) {
            Log.e("EXception: ", e.localizedMessage)
            handleForbiddenResponse()
            null
        }
    }

    private fun toBase64(bytes: ByteArray): String {
        Log.i("encrypted_string", Base64.encodeToString(bytes, Base64.NO_WRAP))
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    //Encryption for Shared Preferences Data
    fun encrypt(jsonObjectString: String?): String? {
        return try {
            Log.e("encryption_original", jsonObjectString?:"")
            val key = keyThree
            Log.e("encryption_key", key)
            val keySkec = key.toByteArray()
            val skc = SecretKeySpec(keySkec, "AES")
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, skc)
            toBase64(cipher.doFinal(jsonObjectString?.toByteArray(StandardCharsets.UTF_8)))
        } catch (e: Exception) {
            Log.e("EXception: ", e.localizedMessage)
            null
        }
    }

    //Decryption for Shared Preferences Data
    fun decrypt(strToDecryptTemp : String?): String? {
        val strToDecrypt = strToDecryptTemp?:""
        return try {
            val key = keyThree
            Log.e("decryption_key", key)
            val keySkec = key.toByteArray()
            val skc = SecretKeySpec(keySkec, "AES")
            val c = Cipher.getInstance("AES/ECB/PKCS5Padding")
            c.init(Cipher.DECRYPT_MODE, skc)
            val decryptValue = Base64.decode(strToDecrypt, Base64.NO_WRAP)
            val decValue = c.doFinal(decryptValue)
            val decryptedValue = String(decValue)
            Log.e("decrypted_string", decryptedValue)
            decryptedValue
        } catch (e: Exception) {
            Log.e("EXception: ", e.localizedMessage)
            decryptByDefaultToken(strToDecrypt)
        }
    }

    fun getDecryptedDataForChat(strToDecrypt: String): String {
        return try {
            val key: String? = if (BuildConfig.FLAVOR.contains("prod")) {
                getChatProd()
            } else if (BuildConfig.FLAVOR.contains("uat")) {
                getChatUAT()
            } else if (BuildConfig.FLAVOR.contains("dev")) {
                getChatDevOther()
            } else {
                getChatTestOther()
            }
            Log.e("decryption_key", key!!)
            val keySkec = key.toByteArray()
            val skc = SecretKeySpec(keySkec, "AES")
            val c = Cipher.getInstance("AES/ECB/PKCS5Padding")
            c.init(Cipher.DECRYPT_MODE, skc)
            val decryptValue = Base64.decode(strToDecrypt, Base64.NO_WRAP)
            val decValue = c.doFinal(decryptValue)
            val decryptedValue = String(decValue)
            Log.e("decrypted_string", decryptedValue)
            decryptedValue
        } catch (e: Exception) {
            strToDecrypt
        }
    }

    //Encrypt the give string using specif key
    fun getEncryptedDataForChat(jsonObjectString: String): String? {
        return try {
            instance
            val key: String?
            key = if (BuildConfig.FLAVOR.contains("prod")) {
                getChatProd()
            } else if (BuildConfig.FLAVOR.contains("uat")) {
                getChatUAT()
            } else if (BuildConfig.FLAVOR.contains("dev")) {
                getChatDevOther()
            } else {
                getChatTestOther()
            }
            Log.e("encryption_token", key!!)
            val keySkec = key.toByteArray()
            val skc = SecretKeySpec(keySkec, "AES")
            val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, skc)
            Log.i(
                "encryption_key_to_send",
                toBase64(cipher.doFinal(jsonObjectString.toByteArray(StandardCharsets.UTF_8)))
            )
            toBase64(cipher.doFinal(jsonObjectString.toByteArray(StandardCharsets.UTF_8)))
        } catch (e: Exception) {
            Log.e("EXception: ", e.localizedMessage)
            null
        }
    }
}