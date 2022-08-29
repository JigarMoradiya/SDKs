package com.example.iiifa_fan_android.ui.view.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.iiifa_fan_android.data.dataprovider.SideMenu
import com.example.iiifa_fan_android.databinding.ActivityPrivacyPolicyBinding
import com.example.iiifa_fan_android.ui.view.base.BaseActivity
import com.example.iiifa_fan_android.ui.view.login.activities.LoginActivity
import com.example.iiifa_fan_android.utils.CustomViews
import com.example.iiifa_fan_android.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyPolicyActivity : BaseActivity() {

    private lateinit var binding: ActivityPrivacyPolicyBinding
    companion object {
        fun getInstance(context: Context?, menuItem: SideMenu) {
            Intent(context, PrivacyPolicyActivity::class.java).apply {
                putExtra("type", menuItem.tag)
                putExtra("title", menuItem.menuTitle)
                context?.startActivity(this)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        initListener()

    }

    private fun initViews() {
        binding.tvTitle.text = intent.getStringExtra("title")
    }

    private fun initListener() {
        binding.ibBack.onClick { onBackPressed() }
    }


    private fun startObserver() {
//        viewModel.getCMSResponse.observe(viewLifecycleOwner, Observer {
//            when (it) {
//                is Resource.Loading -> {
//                    CustomViews.startButtonLoading(requireActivity(), false)
//                }
//                is Resource.Success -> {
//                    CustomViews.hideButtonLoading()
//                    setData(it.value)
//                }
//                is Resource.Failure -> {
//                    CustomViews.hideButtonLoading()
//                    showFailerToast(it.errorBody.toString())
//                }
//            }
//        })
    }


    private fun showFailerToast(error: String) {
        CustomViews.showFailToast(layoutInflater, error)
    }


//    private fun setData(cmsVal: CMS) {
//        when (type) {
//            Constants.PRIVACY_POLICY -> {
//                binding.tvContent.text = cmsVal.privacy_policy_patient.content
//            }
//            Constants.TERMS_CONDITION -> {
//                binding.tvContent.text = cmsVal.terms_and_condition_patient.content
//            }
//            Constants.ABOUT_US -> {
//                binding.tvContent.text = cmsVal.about_us.content
//            }
//            Constants.CANCELLATION_POLICY -> {
//                binding.tvContent.text = cmsVal.refund_cancellation_policy_patient?.content
//            }
//        }
//    }

    private fun getCmsPagesApi() {
        val params: MutableMap<String?, Any?> = java.util.HashMap()

        if (!prefManager.user.isNullOrBlank()) {
            params["user_id"] = prefManager.userId
        }

//        params["user_type"] = Constants.ENTITY_TYPE
//        viewModel.getCMS(params)

    }

}