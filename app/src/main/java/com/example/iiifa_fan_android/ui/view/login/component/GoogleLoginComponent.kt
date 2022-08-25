package com.example.iiifa_fan_android.ui.view.login.component

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.SocialMediaUserModel
import com.example.iiifa_fan_android.databinding.ComponentSocialLoginBinding
import com.example.iiifa_fan_android.ui.view.login.viewmodel.SocialLoginViewModel
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


/** created By Jay Shah 28July,2021*/
class GoogleLoginComponent @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                     defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    val binding = ComponentSocialLoginBinding.inflate(LayoutInflater.from(context), this, true)
    private lateinit var googleLoginViewModel: SocialLoginViewModel
    private var mGoogleApiClient: GoogleSignInClient? = null
    private var socialMediaUserModel: SocialMediaUserModel? = null
    var startActivityResultListener: StartActivityResult? = null

    init {
        if (context is AppCompatActivity) {
            binding.lifecycleOwner = context
        } else if (context is ContextWrapper) {
            binding.lifecycleOwner = context.baseContext as AppCompatActivity
        }
        binding.executePendingBindings()


        initView()
        initGoogleLogin()

    }

    fun setListener(signInActivity: StartActivityResult) {
        startActivityResultListener = signInActivity
    }

    private fun initView() {
        binding.ivSocial.icon = ContextCompat.getDrawable(context, R.drawable.ic_google)
        binding.ivSocial.onClick { newGoogleLogin() }
    }

    /**
     * get ViewModel from Activity/fragment
     */
    fun setViewModel(googleLoginViewModel: SocialLoginViewModel) {
        this.googleLoginViewModel = googleLoginViewModel
    }

    /**
     * get data from Google Login Using activityResult
     */

    fun activityResult(data: Intent?) {
        startActivityResultListener?.showHideProgress(true)
        val result = data?.let { Auth.GoogleSignInApi.getSignInResultFromIntent(it) }
        if (result!!.isSuccess) {
            val account = result.signInAccount

            if (account != null) {

                socialMediaUserModel = SocialMediaUserModel().apply {
                    account.also {
                        social_type = "google"
                        email = it.email
                        fullName = it.displayName
                        first_name = if (it.displayName?.contains(" ") == true) it.displayName?.split(" ".toRegex())?.toTypedArray()?.get(0) else it.displayName
                        last_name = if (it.displayName?.contains(" ") == true) it.displayName?.split(" ".toRegex())?.toTypedArray()?.get(1) else it.displayName
                        profile_pic = if (it.photoUrl != null && !it.photoUrl?.toString().equals("null", ignoreCase = true)) it.photoUrl?.toString() else ""
                    }
                }
                googleLoginViewModel.setSocialMediaLogin(socialMediaUserModel)
            }
        } else {
            startActivityResultListener?.showHideProgress(false)
        }
    }

    /**
     * init Google Login
     */
    private fun initGoogleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestProfile().requestEmail().build()
        mGoogleApiClient = GoogleSignIn.getClient(context, gso)
    }


    /**
     * Set new Google Login
     */
    private fun newGoogleLogin() {
        startActivityResultListener?.showHideProgress(true)
        mGoogleApiClient?.signOut()?.addOnCompleteListener {
            val signInIntent: Intent? = mGoogleApiClient?.signInIntent
            startActivityResultListener?.onStart(signInIntent)
        }
    }

    interface StartActivityResult {
        fun onStart(signInIntent: Intent?)
        fun showHideProgress(isShow: Boolean)
    }


}