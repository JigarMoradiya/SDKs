package com.sdk.network.customs


import android.content.Context
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import com.sdk.ConnectedMindSDK
import com.sdk.utils.CustomFunctions
import com.sdk.utils.PrefManager
import java.nio.charset.StandardCharsets
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


object EncryptRequestData {
    private var encryptRequestData: EncryptRequestData? = null
    private var prefManager: PrefManager? = null
    private val secretKey: SecretKeySpec? = null
    private val key: ByteArray? = null

    val instance: EncryptRequestData?
        get() {
            if (encryptRequestData == null) {
                synchronized(EncryptRequestData::class.java) {
                    if (encryptRequestData == null) {
                        encryptRequestData = EncryptRequestData
                        prefManager = ConnectedMindSDK().instance?.let { PrefManager(it) }
                    }
                }
            }
            return encryptRequestData
        }
    //Encrypt the give string using specif key
    fun getEncryptedData(context: Context, jsonObjectString: String): String? {
        return try {
            Log.d("encryption_original", jsonObjectString)
            instance
            var key: String = CustomFunctions.keyThree
            if (prefManager != null && prefManager?.getToken() != null && !TextUtils.isEmpty(
                    prefManager?.getToken())) key = prefManager?.getToken()!!
            Log.d("encryption_key", key)
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
    fun getDecryptedData(strToDecrypt: String?): String? {
        return try {
            var key: String = CustomFunctions.keyThree
            if (prefManager != null && prefManager?.getToken() != null && !TextUtils.isEmpty(
                    prefManager?.getToken()
                )
            ) key = prefManager?.getToken()!!
            Log.d("decryption_key", key)
            val keySkec = key.toByteArray()
            val skc = SecretKeySpec(keySkec, "AES")
            val c = Cipher.getInstance("AES/ECB/PKCS5Padding")
            c.init(Cipher.DECRYPT_MODE, skc)
            val decryptValue = Base64.decode(strToDecrypt, Base64.NO_WRAP)
            val decValue = c.doFinal(decryptValue)
            val decryptedValue = String(decValue)
            Log.d("decrypted_string", decryptedValue)
            decryptedValue
        } catch (e: Exception) {
            Log.e("EXception: ", e.localizedMessage)
            decryptByDefaultToken(strToDecrypt)
        }
    }

    //decrypy using default token
    fun decryptByDefaultToken(strToDecrypt: String?): String? {
        return try {
            val key: String = CustomFunctions.keyThree
            Log.d("decryption_key", key)
            val keySkec = key.toByteArray()
            val skc = SecretKeySpec(keySkec, "AES")
            val c = Cipher.getInstance("AES/ECB/PKCS5Padding")
            c.init(Cipher.DECRYPT_MODE, skc)
            val decryptValue = Base64.decode(strToDecrypt, Base64.NO_WRAP)
            val decValue = c.doFinal(decryptValue)
            val decryptedValue = String(decValue)
            Log.d("decrypted_string", decryptedValue)
            decryptedValue
        } catch (e: Exception) {
            Log.e("EXception: ", e.localizedMessage)
            null
        }
    }

    private fun toBase64(bytes: ByteArray): String {
        Log.i("encrypted_string", Base64.encodeToString(bytes, Base64.NO_WRAP))
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }

    //Encryption for Shared Preferences Data
    fun encrypt(jsonObjectString: String): String? {
        return try {
            Log.d("encryption_original", jsonObjectString)
            val key: String = CustomFunctions.keyThree
            Log.d("encryption_key", key)
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

    //Decryption for Shared Preferences Data
    fun decrypt(strToDecrypt: String?): String? {
        return try {
            val key: String = CustomFunctions.keyThree
            Log.d("decryption_key", key)
            val keySkec = key.toByteArray()
            val skc = SecretKeySpec(keySkec, "AES")
            val c = Cipher.getInstance("AES/ECB/PKCS5Padding")
            c.init(Cipher.DECRYPT_MODE, skc)
            val decryptValue = Base64.decode(strToDecrypt, Base64.NO_WRAP)
            val decValue = c.doFinal(decryptValue)
            val decryptedValue = String(decValue)
            Log.d("decrypted_string", decryptedValue)
            decryptedValue
        } catch (e: Exception) {
            Log.e("EXception: ", e.localizedMessage)
            decryptByDefaultToken(strToDecrypt)
        }
    }

}
