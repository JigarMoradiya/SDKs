package com.jigar.me.ui.view.dashboard.fragments.starts

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jigar.me.R
import com.jigar.me.databinding.FragmentSplashBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.extensions.isNetworkAvailable
import com.jigar.me.utils.extensions.toastL
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.HashMap

@AndroidEntryPoint
class SplashFragment : BaseFragment() {
    private lateinit var binding: FragmentSplashBinding

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        if (prefManager.getBaseUrl().isEmpty()) {
            if (requireContext().isNetworkAvailable){
                getFBConstant()
            }else{
                requireContext().toastL(resources.getString(R.string.no_internet))
                requireActivity().finish()
            }
        } else {
            nextPage()
        }
    }

    private fun getFBConstant() {
        val databaseReference = FirebaseDatabase.getInstance().reference
            .child(AppConstants.AbacusProgress.Settings)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val mapMessage = dataSnapshot.value as HashMap<*, *>?
                    val reset = mapMessage!![AppConstants.AbacusProgress.reset] as Long
                    val discount = mapMessage[AppConstants.AbacusProgress.Discount] as Long
                    val resetImage = mapMessage[AppConstants.AbacusProgress.resetImage] as Long
                    val privacyPolicy = mapMessage[AppConstants.AbacusProgress.Privacy_Policy_data] as String
                    val baseUrl = mapMessage[AppConstants.AbacusProgress.BaseUrl] as String
                    val ads = mapMessage[AppConstants.AbacusProgress.Ads] as String
                    val ratePopup = mapMessage[AppConstants.rateSetting.shouldShowRatePopup] as Boolean

                    with(prefManager){
                        setBaseUrl(baseUrl)
                        setCustomParam(AppConstants.AbacusProgress.Privacy_Policy_data,privacyPolicy)
                        setCustomParamBoolean(AppConstants.rateSetting.shouldShowRatePopup,ratePopup)
                        setCustomParam(AppConstants.AbacusProgress.Ads,ads)
                        setCustomParamInt(AppConstants.AbacusProgress.Discount,discount.toInt())

                        if (reset.toInt() > getCustomParamInt(AppConstants.AbacusProgress.reset, 0)) {
                            setCustomParamInt(AppConstants.AbacusProgress.reset, reset.toInt())
                            setCustomParam(AppConstants.Extras_Comman.Level + "2","")
                            setCustomParam(AppConstants.Extras_Comman.Level + "3","")
                        }

                        if (resetImage.toInt() > getCustomParamInt(AppConstants.AbacusProgress.resetImage, 0)) {
                            setCustomParamInt(AppConstants.AbacusProgress.resetImage, resetImage.toInt())
                            setCustomParam(AppConstants.Extras_Comman.DownloadType+"_"+AppConstants.Extras_Comman.DownloadType_Maths, "")
                            setCustomParam(AppConstants.Extras_Comman.DownloadType+"_"+AppConstants.Extras_Comman.DownloadType_Nursery, "")
                        }
                    }
                    nextPage()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun nextPage() {
        lifecycleScope.launch {
            delay(2000)
            mNavController.navigate(R.id.action_splashFragment_to_homeFragment)
        }
    }
}