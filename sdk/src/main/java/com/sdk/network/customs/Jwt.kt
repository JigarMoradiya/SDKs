package com.sdk.network.customs

import android.util.Log
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.security.Key
import java.util.*
import javax.crypto.spec.SecretKeySpec

object Jwt {
    fun createJWT(
        headers_jwt: MutableMap<String, Any>?,
        bodyString: String,
        ttlMillis: Long,
        key: String
    ): String {

        //The JWT signature algorithm we will be using to sign the token
        val signatureAlgorithm = SignatureAlgorithm.HS256
        val nowMillis = System.currentTimeMillis()
        val now = Date(nowMillis)
        Log.d("post_request_key", key)

        //We will sign our JWT with our ApiKey secret
        val keySkec = key.toByteArray()
        val signingKey: Key = SecretKeySpec(keySkec, signatureAlgorithm.jcaName)
        val params = HashMap<String, Any>()
        params["params"] = bodyString

        //Let's set the JWT Claims
        val builder = Jwts.builder()
            .setHeader(headers_jwt)
            .addClaims(params)
            .setIssuedAt(now)
            .signWith(signingKey, signatureAlgorithm)

        //if it has been specified, let's add the expiration
        if (ttlMillis > 0) {
            val expMillis = nowMillis + ttlMillis
            val exp = Date(expMillis)
            builder.setExpiration(exp)
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact()
    }


    fun decodeJWT(jwt: String?, key: String): String {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Log.d("post_response_key", key)
        return try {
            val claimsJws = Jwts.parserBuilder()
                .setSigningKey(key.toByteArray())
                .build()
                .parseClaimsJws(jwt)
            claimsJws.body["params"].toString()
        } catch (e: Exception) {
            throw e
        }
    }

}