package com.example.iiifa_fan_android.utils;

import android.util.Log;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class Jwt {

    public static String createJWT(Map<String, Object> headers_jwt, String bodyString, long ttlMillis, String key) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        Log.e("post_request_key", key);

        //We will sign our JWT with our ApiKey secret
        byte[] keySkec = key.getBytes();
        Key signingKey = new SecretKeySpec(keySkec, signatureAlgorithm.getJcaName());

        HashMap<String, Object> params = new HashMap<>();
        params.put("params", bodyString);

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setHeader(headers_jwt)
                .addClaims(params)
                .setIssuedAt(now)
                .signWith(signingKey, signatureAlgorithm);

        //if it has been specified, let's add the expiration
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }


    public static String decodeJWT(String jwt, String key) {
        //This line will throw an exception if it is not a signed JWS (as expected)

        Log.e("post_response_key", key);
        try {
            String claims = (String) Jwts.parserBuilder()
                    .setSigningKey(key.getBytes())
                    .build()
                    .parseClaimsJws(jwt).getBody().get("params");

            return claims;
        } catch (Exception e) {
            throw e;
        }
    }


}
