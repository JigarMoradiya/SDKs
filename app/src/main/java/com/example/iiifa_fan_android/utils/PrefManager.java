package com.example.iiifa_fan_android.utils;

import android.content.Context;
import android.content.SharedPreferences;


//Shared prefences class to store data locally after encryption
public class PrefManager {
    private static final String DOCTOR_ACCOUNT = "doctor_account";
    // Shared preferences file name
    private static final String PREF_NAME = "androidhive-welcome";
    private static final String IS_USER_LOGGED_IN_ONCE = "IS_USER_LOGGED_IN_ONCE";
    private static final String USER_EMAIL = "user_email";
    private static final String TOKEN = "token";
    private static final String USER = "user";
    private static final String USER_ID = "user_id";
    private static final String PREVIOUS_USER_ID = "previous_user_id";
    private static final String NOTIFICATION_TOKEN = "notification_token";
    private static final String PROFILE_URL = "profile_url";
    private static final String ENCRYPTION_ENABLED = "encryption_enabled";
    private static final String VALIDATE_OTP_TOKEN = "validate_otp_token";
    private static final String SHA_STRING = "sha_string";
    private static final String IP_ADDRESS = "ip_address";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // shared pref mode
    int PRIVATE_MODE = 0;

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getIsUserLoggedInOnce() {
        return EncryptRequestData.decrypt(pref.getString(IS_USER_LOGGED_IN_ONCE, ""));
    }

    public void setIsUserLoggedInOnce(String isUserLoggedInOnce) {
        editor.putString(IS_USER_LOGGED_IN_ONCE, EncryptRequestData.encrypt(isUserLoggedInOnce)).apply();
    }


    public String getProfileUrl() {
        return EncryptRequestData.decrypt(pref.getString(PROFILE_URL, ""));
    }

    public void setProfileUrl(String profileUrl) {
        editor.putString(PROFILE_URL, EncryptRequestData.encrypt(profileUrl)).apply();
    }


    public String getUserId() {
        return EncryptRequestData.decrypt(pref.getString(USER_ID, ""));
    }

    public void setUserId(String userId) {
        editor.putString(USER_ID, EncryptRequestData.encrypt(userId)).apply();
    }


    public String getPreviousUserId() {
        return EncryptRequestData.decrypt(pref.getString(PREVIOUS_USER_ID, ""));
    }

    public void setPreviousUserId(String previousUserId) {
        editor.putString(PREVIOUS_USER_ID, EncryptRequestData.encrypt(previousUserId)).apply();
    }


    public String getUserEmail() {
        return EncryptRequestData.decrypt(pref.getString(USER_EMAIL, ""));
    }

    public void setUserEmail(String userEmail) {
        editor.putString(USER_EMAIL, EncryptRequestData.encrypt(userEmail)).apply();
    }

    public String getIpAddress() {
        return EncryptRequestData.decrypt(pref.getString(IP_ADDRESS, ""));
    }

    public void setIpAddress(String ipAddress) {
        editor.putString(IP_ADDRESS, EncryptRequestData.encrypt(ipAddress)).apply();
    }

    public String getUser() {
        return EncryptRequestData.decrypt(pref.getString(USER, ""));
    }

    public void setUser(String user) {
        editor.putString(USER, EncryptRequestData.encrypt(user)).apply();
    }

    public String getToken() {
        return EncryptRequestData.decrypt(pref.getString(TOKEN, ""));
    }

    public void setToken(String token) {
        editor.putString(TOKEN, EncryptRequestData.encrypt(token)).apply();
    }

    public String getEncryptionEnabled() {
        return EncryptRequestData.decrypt(pref.getString(ENCRYPTION_ENABLED, ""));
    }

    public void setEncryptionEnabled(String encryptionEnabled) {
        editor.putString(ENCRYPTION_ENABLED, EncryptRequestData.encrypt(encryptionEnabled)).apply();
    }


    public String getNotificationToken() {
        return EncryptRequestData.decrypt(pref.getString(NOTIFICATION_TOKEN, ""));
    }

    public void setNotificationToken(String notificationToken) {
        editor.putString(NOTIFICATION_TOKEN, EncryptRequestData.encrypt(notificationToken)).apply();
    }



    public String getValidateOtpToken() {
        return EncryptRequestData.decrypt(pref.getString(VALIDATE_OTP_TOKEN, ""));
    }

    public void setValidateOtpToken(String validateOtpToken) {
        editor.putString(VALIDATE_OTP_TOKEN, EncryptRequestData.encrypt(validateOtpToken)).apply();
    }

    public String getCertSha() {
        return EncryptRequestData.decrypt(pref.getString(SHA_STRING, ""));
    }

    public void setCertSha(String sha) {
        editor.putString(SHA_STRING, EncryptRequestData.encrypt(sha)).apply();
    }

}
