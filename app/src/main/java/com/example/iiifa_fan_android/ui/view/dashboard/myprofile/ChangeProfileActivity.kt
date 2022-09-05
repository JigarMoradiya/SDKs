package com.example.iiifa_fan_android.ui.view.dashboard.myprofile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.FanUser
import com.example.iiifa_fan_android.databinding.ActivityChangeProfileBinding
import com.example.iiifa_fan_android.databinding.ActivityEditProfileBinding
import com.example.iiifa_fan_android.ui.view.base.BaseActivity
import com.example.iiifa_fan_android.ui.view.commonapiscalls.UploadToS3fromSignedURL
import com.example.iiifa_fan_android.ui.view.commonapiscalls.interfaces.UploadToS3Interface
import com.example.iiifa_fan_android.ui.viewmodel.CommonViewModel
import com.example.iiifa_fan_android.ui.viewmodel.FanViewModel
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomFunctions
import com.example.iiifa_fan_android.utils.CustomViews
import com.example.iiifa_fan_android.utils.Resource
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.apache.commons.io.FilenameUtils
import java.io.File

@AndroidEntryPoint
class ChangeProfileActivity : BaseActivity(), UploadToS3Interface {

    private lateinit var binding: ActivityChangeProfileBinding
    private var checkCurrentRequest = -1
    private var pictureImagePath: String? = null
    private var user: FanUser? = null
    private val commonViewModel by viewModels<CommonViewModel>()
    private val viewModel by viewModels<FanViewModel>()
    companion object {
        fun getInstance(context: Context?) {
            Intent(context, ChangeProfileActivity::class.java).apply {
                context?.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangeProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initListener()
        initObserver()
    }

    private fun initViews() {
        user = Gson().fromJson(prefManager.getUserData(), FanUser::class.java)
    }

    private fun initListener() {
        binding.ibBack.onClick { onBackPressed() }
        binding.tvRemove.onClick {
            binding.ivRemove.performClick()
        }
        binding.ivRemove.onClick {
            setImagePreview("")
        }
        binding.tvCamera.onClick {
            binding.ivCamera.performClick()
        }
        binding.ivCamera.onClick {
            checkCurrentRequest = Constants.CAMERA_REQUEST

            CustomFunctions.checkPermissions(
                this@ChangeProfileActivity,
                Constants.CAMERA_EXTERNAL_READ,
                requestMultiplePermissions,
                checkCurrentRequest,
                resultLauncher
            )

        }

        binding.tvGallery.onClick {
            binding.ivGallery.performClick()
        }
        binding.ivGallery.onClick {
            checkCurrentRequest = Constants.PICK_IMAGE_MULTIPLE

            CustomFunctions.checkPermissions(
                this@ChangeProfileActivity,
                Constants.EXTERNAL_READ,
                requestMultiplePermissions,
                checkCurrentRequest,
                resultLauncher,
                false
            )
        }
        binding.tvDone.onClick {
            validateDetails()
        }
    }
    private fun initObserver() {
        viewModel.updateFanProfileResponse.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    CustomViews.startButtonLoading(this@ChangeProfileActivity, false)
                }
                is Resource.Success -> {
                    CustomViews.hideButtonLoading()
                    if (it.value.code == 200) {
                        CustomViews.hideButtonLoading()
                        val data = Gson().fromJson(it.value.content!![Constants.DATA], FanUser::class.java)
                        prefManager.setUserData(Gson().toJson(data))
                        CustomViews.showSuccessToast(layoutInflater, it.value.message)
                        val intent = Intent()
                        setResult(RESULT_OK,intent)
                        finish()
                    } else{
                        CustomViews.hideButtonLoading()
                        CustomViews.showFailToast(layoutInflater, it.value.message)
                    }
                }
                is Resource.Failure -> {
                    CustomViews.hideButtonLoading()
                    CustomViews.showFailToast(layoutInflater, getString(R.string.something_went_wrong))
                }
            }
        }

    }
    private var requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            handleOnPermissionResult(permissions)
        }


    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            handleOnActivityResult(result)
        }

    private fun handleOnPermissionResult(permissions: Map<String, Boolean>) {
        CustomFunctions.handleOnPermissionResult(permissions, this, resultLauncher, checkCurrentRequest,layoutInflater,false)
    }

    private fun handleOnActivityResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            Log.e("intenetdata", data.toString())
            setAssetsToTheView(data)
        }
    }

    private fun setAssetsToTheView(data: Intent?) {
        when (checkCurrentRequest) {
            Constants.PICK_IMAGE_MULTIPLE -> {
                if (data != null) {
                    val assetsPath = CustomFunctions.whenImageIsPicked(this, data)
                    Log.e("assets_print", assetsPath.toString())

                    if (assetsPath.isNotEmpty())
                        assetsPath[0]?.let { setImagePreview(it) }
                }
            }
            Constants.CAMERA_REQUEST -> {
                val assetsPath = CustomFunctions.whenImageIsCaptured(this)
                Log.e("assets_print", assetsPath.toString())

                if (assetsPath.isNotEmpty())
                    assetsPath[0]?.let { setImagePreview(it) }
            }
        }
    }

    private fun setImagePreview(url: String?) {
        pictureImagePath = url
        Glide.with(this)
            .load(url)
            .apply(RequestOptions().placeholder(R.drawable.ic_default_user))
            .into(binding.ivUserProfile)
    }

    private fun validateDetails() {

        when {
            user?.profile_url.isNullOrEmpty() && !pictureImagePath.isNullOrBlank() -> {
                setParamsToGetPutObject()
            }


            !user?.profile_url.isNullOrBlank() && user?.profile_url != pictureImagePath -> {

                if (pictureImagePath.isNullOrBlank()) {
                    callUpdateProfileAPI("")
                } else {
                    setParamsToGetPutObject()
                }

            }
            else -> {
                binding.ibBack.performClick()
            }
        }
    }

    private fun setParamsToGetPutObject() {
        val extension = FilenameUtils.getExtension(pictureImagePath)
        val _fileName = prefManager.getUserId() + "_" + System.currentTimeMillis()
        val file = File(pictureImagePath)
        val folderName = Constants.PROFILE_IMAGE
        val mediaType = Constants.IMAGE

        UploadToS3fromSignedURL(commonViewModel,this, this, this, isPublicAssets = true)
            .setParamsToGetPutObject(_fileName, folderName, extension, mediaType, file)
    }

    override fun onSuccess(response: String, uploadedFileName: String, position: Int, type: String) {
        callUpdateProfileAPI(uploadedFileName)
    }

    override fun onFailure(error: String) {
        showFailerToast(error)
    }
    private fun callUpdateProfileAPI(url: String) {
        CustomViews.startButtonLoading(this, false)
        val params: MutableMap<String?, Any?> = java.util.HashMap()
        params["fan_id"] = prefManager.getUserId()
        params["profile_url"] = url
        viewModel.updateFanProfile(params)
    }
    private fun showFailerToast(error: String) {
        CustomViews.showFailToast(layoutInflater, error)
    }
}