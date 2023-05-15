package com.jigar.me.ui.view.login.fragments

import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jigar.me.BuildConfig
import com.jigar.me.R
import com.jigar.me.databinding.BottomSheetCommanConfimationBinding
import com.jigar.me.databinding.FragmentSplashBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.ui.view.confirm_alerts.bottomsheets.CommonConfirmationBottomSheet
import com.jigar.me.ui.view.dashboard.MainDashboardActivity
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
        val androidId = Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        prefManager.setDeviceId(androidId)
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
                    val resetImage = mapMessage!![AppConstants.AbacusProgress.resetImage] as Long
                    val privacyPolicy = mapMessage[AppConstants.AbacusProgress.Privacy_Policy_data] as String
                    val baseUrl = mapMessage[AppConstants.AbacusProgress.BaseUrl] as String
                    val iPath = mapMessage[AppConstants.AbacusProgress.iPath] as String
                    val ads = mapMessage[AppConstants.AbacusProgress.Ads] as String
                    val isAdmob = if (BuildConfig.DEBUG){
                        false
                    }else{
                        mapMessage[AppConstants.AbacusProgress.isAdmob] as Boolean
                    }
                    with(prefManager){
                        setCustomParamBoolean(AppConstants.AbacusProgress.isAdmob,isAdmob)
                        setBaseUrl(baseUrl)
                        setCustomParam(AppConstants.AbacusProgress.iPath,iPath)
                        setCustomParam(AppConstants.AbacusProgress.Privacy_Policy_data,privacyPolicy)
                        setCustomParam(AppConstants.AbacusProgress.Ads,ads)

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
            delay(3000)
            MainDashboardActivity.getInstance(requireContext())
        }
    }
}