package com.example.iiifa_fan_android.utils;


import com.google.gson.JsonObject;

import java.util.HashMap;

public class DownloadPreSignUrl {

//    private static MainApiResponseInterface mainApiResponseInterface;
//    private static MainApiCall mainApiCall = MainApiCall.getInstance1();
//
//    public static void downloadSignedUrl(String file_key, DownloadFromS3ResponseInterface downloadFromS3ResponseInterface) {
//        mainApiResponseInterface = new MainApiResponseInterface() {
//            @Override
//            public void onSuccess(JsonObject successResponse, String apiName) {
//                String signed_url = null;
//                if (apiName.equalsIgnoreCase(com.example.iiifa_fan_android.utils.Constants.GET_S3_DOWNLOAD_URL)) {
//                    signed_url = successResponse.get(com.example.iiifa_fan_android.utils.Constants.DATA).getAsJsonObject().get("s3signedGetUrl").getAsString();
//                }
//                downloadFromS3ResponseInterface.onSuccess(signed_url);
//            }
//
//
//            @Override
//            public void onFailure(Error failureMessage, String apiName) {
//                downloadFromS3ResponseInterface.onFailure(failureMessage.getUserMessage());
//            }
//        };
//
//        HashMap<String, Object> params = new HashMap<>();
//        params.put("file_key", file_key);
//
//        mainApiCall.getData(params,com.example.iiifa_fan_android.utils.Constants.GET_S3_DOWNLOAD_URL, mainApiResponseInterface);
//    }
}
