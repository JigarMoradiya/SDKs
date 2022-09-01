package com.example.iiifa_fan_android.ui.view.login.component


import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.SocialMediaUserModel
import com.example.iiifa_fan_android.databinding.ComponentSocialLoginBinding
import com.example.iiifa_fan_android.ui.viewmodel.SocialLoginViewModel
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider


/** created By Jigar Moradiya 25 Aug,2022*/

class AppleLoginComponent @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    val binding = ComponentSocialLoginBinding.inflate(LayoutInflater.from(context), this, true)
    private var socialMediaUserModel: SocialMediaUserModel? = null
    private lateinit var socialLoginViewModel: SocialLoginViewModel
    private lateinit var mAuth: FirebaseAuth
    private lateinit var activity : AppCompatActivity
    init {
        if (context is AppCompatActivity) {
            binding.lifecycleOwner = context
        } else if (context is ContextWrapper) {
            binding.lifecycleOwner = context.baseContext as AppCompatActivity
        }
        binding.executePendingBindings()

        initView()

    }

    private fun initView() {
//        FirebaseApp.initializeApp(context)
//        mAuth = FirebaseAuth.getInstance()
        binding.ivSocial.icon = ContextCompat.getDrawable(context, R.drawable.ic_apple)
        binding.ivSocial.onClick {
            socialLoginViewModel.isLoader.postValue(true)
            if (::activity.isInitialized){
//                appleLogin()
            }
        }


    }

    /**
     * get ViewModel from Activity/fragment
     */

    fun setViewModel(facebookLoginViewModel: SocialLoginViewModel) {
        this.socialLoginViewModel = facebookLoginViewModel
    }

    fun setListener(signInActivity: AppCompatActivity) {
        activity = signInActivity
    }


    /**
     * apple login
     */
    private fun appleLogin() {
        val provider = OAuthProvider.newBuilder("apple.com")

        mAuth.startActivityForSignInWithProvider(activity, provider.build())
            .addOnSuccessListener { authResult ->
                // Sign-in successful!
                Log.e("post_apple", "activitySignIn:onSuccess:" + authResult.user)
                val user = authResult.user
                socialMediaUserModel = SocialMediaUserModel().apply {
                    social_type = "apple"
                    email = user?.email?:""
                    social_id = user?.uid
                    if (user?.displayName != null) {
                        first_name = user.displayName!!.substring(0, user.displayName!!.lastIndexOf(" "))
                        last_name = user.displayName!!.substring(user.displayName!!.lastIndexOf(" ") + 1)
                    }
                    if (user?.photoUrl != null){
                        profile_pic = user.photoUrl.toString()
                    }
                }
                socialLoginViewModel.setSocialMediaLogin(socialMediaUserModel)
                // ...
            }
            .addOnFailureListener { e -> Log.w("post_apple", "activitySignIn:onFailure", e) }
    }


}