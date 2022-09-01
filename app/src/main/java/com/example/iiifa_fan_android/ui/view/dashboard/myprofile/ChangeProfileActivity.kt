package com.example.iiifa_fan_android.ui.view.dashboard.myprofile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.databinding.ActivityChangeProfileBinding
import com.example.iiifa_fan_android.databinding.ActivityEditProfileBinding
import com.example.iiifa_fan_android.ui.view.base.BaseActivity
import com.example.iiifa_fan_android.utils.Constants
import com.example.iiifa_fan_android.utils.CustomFunctions
import com.example.iiifa_fan_android.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ChangeProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityChangeProfileBinding
    private var checkCurrentRequest = -1
    private var pictureImagePath: String? = null
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

    }

    private fun initViews() {
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
            .into(binding.ivProfile)
    }
}