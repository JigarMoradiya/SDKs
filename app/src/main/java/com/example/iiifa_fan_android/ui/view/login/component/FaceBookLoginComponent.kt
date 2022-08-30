package com.example.iiifa_fan_android.ui.view.login.component


import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.iiifa_fan_android.data.models.SocialMediaUserModel
import com.example.iiifa_fan_android.databinding.ComponentSocialLoginBinding
import com.example.iiifa_fan_android.ui.viewmodel.SocialLoginViewModel
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.example.iiifa_fan_android.utils.extensions.toastS
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult


/** created By Jigar Moradiya 25 Aug,2022*/

class FaceBookLoginComponent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    val binding = ComponentSocialLoginBinding.inflate(LayoutInflater.from(context), this, true)
    private var callbackManager: CallbackManager? = null
    private var socialMediaUserModel: SocialMediaUserModel? = null
    private lateinit var socialLoginViewModel: SocialLoginViewModel

    init {
        if (context is AppCompatActivity) {
            binding.lifecycleOwner = context
        } else if (context is ContextWrapper) {
            binding.lifecycleOwner = context.baseContext as AppCompatActivity
        }
        binding.executePendingBindings()

        initView()
        initFaceBookLogin()
    }

    private fun initView() {
        binding.ivSocial.icon = ContextCompat.getDrawable(context,com.example.iiifa_fan_android.R.drawable.ic_facebook)
        binding.ivSocial.onClick {
            socialLoginViewModel.isLoader.postValue(true)
            LoginManager.getInstance().logOut()
            LoginManager.getInstance().logInWithReadPermissions(context as AppCompatActivity, listOf("public_profile", "email"))
        }


    }

    /**
     * get ViewModel from Activity/fragment
     */

    fun setViewModel(facebookLoginViewModel: SocialLoginViewModel) {
        this.socialLoginViewModel = facebookLoginViewModel
    }


    /**
     * init face book login
     */
    private fun initFaceBookLogin() {
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

                override fun onSuccess(result: LoginResult) {
                    val request = GraphRequest.newMeRequest(result.accessToken) { user, response ->
                            if (response?.error == null) {
                                socialMediaUserModel = SocialMediaUserModel().apply {
                                    social_type = "facebook"
                                    email = if (!user?.optString("email").equals("null", ignoreCase = true)) user?.optString("email") else ""
                                    social_id = user?.optString("id")
                                    social_token = result.accessToken.token
                                    first_name = if (!user?.optString("first_name").equals("null", ignoreCase = true)) user?.optString("first_name") else ""
                                    last_name = if (!user?.optString("last_name").equals("null", ignoreCase = true)) user?.optString("last_name") else ""
                                    fullName = if (!user?.optString("name").equals("null", ignoreCase = true)) user?.optString("name") else ""
                                    profile_pic = user?.getJSONObject("picture")?.getJSONObject("data")?.optString("url")
                                }
                                socialLoginViewModel.setSocialMediaLogin(socialMediaUserModel)

                            }
                        }
                    val parameters = Bundle()
                    parameters.putString(
                        "fields", "id,first_name,last_name,picture.width(400).height(400),name,email"
                    )
                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCancel() {}
                override fun onError(error: FacebookException) {
                    context.toastS(error.localizedMessage)
                    error.printStackTrace()
                }
            })
    }

    fun activityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager?.onActivityResult(requestCode, resultCode, data)
    }


}