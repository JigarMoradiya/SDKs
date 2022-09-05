package com.example.iiifa_fan_android.ui.view.commonapiscalls

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import com.example.iiifa_fan_android.data.models.Asset
import com.example.iiifa_fan_android.data.pref.PreferencesHelper
import com.example.iiifa_fan_android.ui.view.commonapiscalls.interfaces.UploadMultipleFilesToS3Interface
import com.example.iiifa_fan_android.ui.view.commonapiscalls.interfaces.UploadToS3Interface
import com.example.iiifa_fan_android.ui.viewmodel.CommonViewModel
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomFunctions
import org.apache.commons.io.FilenameUtils
import java.io.File

class UploadMultipleFilesToS3(
    val context: Context,
    val viewModel: CommonViewModel,
    val activity: Activity,
    val lifecycleOwner: LifecycleOwner,
    var assets: ArrayList<Asset>,
    val prefManager: PreferencesHelper,
    val uploadMultipleFilesToS3Interface: UploadMultipleFilesToS3Interface,
    val isChatData: Boolean = false
) : UploadToS3Interface {


    fun uploadAssets() {
        val position = 0
        val asset = assets[position]

        Log.d("initial_assets", assets.toString())

        //uploading image as it is
        asset.presigned_url?.let { setParamsToGetPutObject(asset.type, it, position) }
    }


    private fun setParamsToGetPutObject(type: String, path: String, position: Int) {
        val extension = FilenameUtils.getExtension(path)
        val fileName = FilenameUtils.removeExtension(File(path).name)
        val _fileName = prefManager.getUserId() + "_" + System.currentTimeMillis()


        val file = File(path)

        var folderName = type
        var mediaType = ""


        if (isChatData) {
            //as of now wea re using multiple upload only for chat so not adding
            mediaType = type
            folderName = CustomFunctions.getFolderNameForChat(type)
        } else {
            when (type) {
                Constants.IMAGE -> {
                    mediaType = Constants.IMAGE
                    folderName = Constants.POST_IMAGE
                }

                Constants.THUMBNAIL -> {
                    mediaType = Constants.IMAGE
                    folderName = Constants.POST_THUMBNAIL
                }

                Constants.VIDEO -> {
                    mediaType = Constants.VIDEO
                    folderName = Constants.POST_VIDEO
                }
            }
        }


        Log.e("file_extention", extension.toString())
        Log.e("file_original_name", fileName)
        Log.e("file_api_name", _fileName)
        Log.e("file_path", path.toString())
        Log.e("file_media_type", mediaType)
        Log.e("file_folder_name", folderName)
        Log.e("file_position", position.toString())

        UploadToS3fromSignedURL(
                viewModel,
                activity,
                lifecycleOwner,
                this,
                position = position
        ).setParamsToGetPutObject(_fileName, folderName, extension, mediaType, file)
    }

    //upload to s3
    override fun onSuccess(response: String, uploadedFileName: String, position: Int, type: String) {


        assets[position].type = type
        when {
            uploadedFileName.contains(Constants.VIDEO_FOLDER) -> {


                val thumbnailurl = CustomFunctions.convertBitmapToFile(
                        context,
                        prefManager.getUserId() + "_" + System.currentTimeMillis(),
                        assets[position].presigned_url
                )


                assets[position].presigned_url = uploadedFileName
                setParamsToGetPutObject(Constants.THUMBNAIL, thumbnailurl, position)

                //save uploaded file key in path

            }
            uploadedFileName.contains(Constants.THUMBNAIL_FOLDER) -> {
                //upload video thumbnail of video is uploaded successfully

                assets[position].thumbnail_path = uploadedFileName
                //because that the main asset type will be the video
                assets[position].type = Constants.VIDEO

                //check before making a new call
                if (position != -1 && position < (assets.size - 1)) {
                    assets[position + 1].presigned_url?.let {
                        setParamsToGetPutObject(
                                assets[position + 1].type,
                                it,
                                position + 1
                        )
                    }
                } else {
                    Log.d("uploaded_assets", assets.toString())
                    uploadMultipleFilesToS3Interface.onSuccessResponse(assets)
                    //finally call create post API
                }

            }
            else -> {
                assets[position].presigned_url = uploadedFileName

                //check before making a new call
                if (position != -1 && position < (assets.size - 1)) {
                    assets[position + 1].presigned_url?.let {
                        setParamsToGetPutObject(
                                assets[position + 1].type,
                                it,
                                position + 1
                        )
                    }
                } else {
                    Log.d("uploaded_assets", assets.toString())
                    uploadMultipleFilesToS3Interface.onSuccessResponse(assets)
                    //finally call create post API
                }
            }
        }

    }

    override fun onFailure(error: String) {
        uploadMultipleFilesToS3Interface.onFailure(error)
    }


}