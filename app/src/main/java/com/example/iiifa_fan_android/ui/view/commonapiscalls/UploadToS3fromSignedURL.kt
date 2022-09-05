package com.example.iiifa_fan_android.ui.view.commonapiscalls

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.api.UploadToS3Client
import com.example.iiifa_fan_android.ui.view.commonapiscalls.interfaces.UploadToS3Interface
import com.example.iiifa_fan_android.ui.viewmodel.CommonViewModel
import com.example.iiifa_fan_android.utils.CustomViews
import com.example.iiifa_fan_android.utils.Resource
import com.google.gson.JsonObject
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.net.URL

class UploadToS3fromSignedURL constructor(
    val viewModel: CommonViewModel,
    val activity: Activity,
    val lifecycleOwner: LifecycleOwner,
    var uploadToS3Interface: UploadToS3Interface,// this is in case we are calling this function in loop
    var position: Int = -1,
    var isPublicAssets: Boolean = false
) {

    private var uploadedFileName: String? = null

    private fun startObserver() {
        if (!viewModel.getSignedObjectPutUrlResponse.hasActiveObservers()) {
            viewModel.getSignedObjectPutUrlResponse.observe(lifecycleOwner) {
                when (it) {
                    is Resource.Loading -> {
                        CustomViews.startButtonLoading(activity, false)
                    }
                    is Resource.Success -> {

                        if (it.value.code == 200) {

                            if (isPublicAssets) {
                                it.value.content?.get("data")?.asJsonObject?.let { data ->
                                    data.get("s3signedPutUrl")?.asString?.let { it ->
                                        val subString = it.replace(URL(it).query, "");
                                        uploadedFileName = subString


                                        //this to update the uploaded file name
                                        activity.runOnUiThread {
                                            viewModel.updateFileName(
                                                position,
                                                uploadedFileName!!
                                            )
                                        }
                                    }
                                }
                            }

                            uploadToS3(
                                it.value.content,
                                viewModel.currentPosition.value!!,
                                viewModel.filePath.value!!, viewModel.fileType.value!!
                            )
                        } else {
                            CustomViews.hideButtonLoading()
                            uploadToS3Interface.onFailure(activity.getString(R.string.something_went_wrong))
                        }
                    }
                    is Resource.Failure -> {
                        CustomViews.hideButtonLoading()
                        uploadToS3Interface.onFailure(it.errorBody.toString())
                    }
                }
            }
        }
    }


    fun setParamsToGetPutObject(
            fileName: String, folderName: String, file_extension: String,
            file_type: String,
            image_file: File
    ) {
        startObserver()
        val params: MutableMap<String?, Any?> = java.util.HashMap()
        uploadedFileName = "$folderName$fileName.$file_extension"
        params["file_key"] = "$folderName$fileName.$file_extension"
        params["file_type"] = "$file_type/$file_extension"


        //we are saving this we need to upload data in response of viewmodel API
        activity.runOnUiThread {
            viewModel.setUrlAndPosition(
                    position,
                    uploadedFileName!!,
                    file_extension,
                    file_type,
                    image_file,
                    isPublicAssets
            )
        }

        viewModel.getSignedObjectPutUrl(params)

    }

    private fun uploadToS3(successResponse: JsonObject?, position: Int, path: String, type: String) {
        Log.d("success_response", successResponse.toString())


        when {
            successResponse != null -> {
                val data =
                        successResponse.get("data")?.asJsonObject?.get("s3signedPutUrl")?.asString

                when {
                    data != null -> {
                        Log.d("file_uploading..", viewModel.imageFile.value!!.absolutePath)
                        Log.d("file_uploading_ext", viewModel.fileExtension.value!!)
                        Log.d("file_uploading_type", viewModel.fileType.value!!)

                        UploadToS3Client.getCall(data, viewModel.fileExtension.value!!, viewModel.fileType.value!!, viewModel.imageFile.value!!)
                                .enqueue(object : Callback {
                                    override fun onFailure(call: Call, e: IOException) {
                                        activity.runOnUiThread {
                                            Log.d("uploadres", e.toString())
                                            CustomViews.hideButtonLoading()
                                            uploadToS3Interface.onFailure("Something went wrong, please try after sometime")
                                        }
                                    }

                                    override fun onResponse(call: Call, response: Response) {

                                        Log.d("uploadres", response.toString())
                                        Log.d("uploadres", path + " " + position)

                                        uploadToS3Interface.onSuccess(
                                                response.toString(),
                                                path, position, type
                                        )

                                    }
                                })
                    }
                    else -> {
                        CustomViews.hideButtonLoading()
                    }
                }
            }
            else -> {
                CustomViews.hideButtonLoading()
            }
        }

    }


}