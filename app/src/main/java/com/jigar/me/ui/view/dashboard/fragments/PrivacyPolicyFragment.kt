package com.jigar.me.ui.view.dashboard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.jigar.me.R
import com.jigar.me.databinding.FragmentPrivacyPolicyBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.utils.AppConstants
import com.jigar.me.utils.extensions.onClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyPolicyFragment : BaseFragment() {
    private lateinit var binding: FragmentPrivacyPolicyBinding
    private lateinit var mNavController: NavController
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentPrivacyPolicyBinding.inflate(inflater, container, false)
        setNavigationGraph()
        initViews()
        initListener()
        return binding.root
    }
    private fun setNavigationGraph() {
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }
    private fun initViews() {
        if (prefManager.getCustomParam(AppConstants.AbacusProgress.Privacy_Policy_data,"").isNotEmpty()){
            binding.txtHtml.text = HtmlCompat.fromHtml(
                prefManager.getCustomParam(AppConstants.AbacusProgress.Privacy_Policy_data,""),
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
        }
    }

    private fun initListener() {
        binding.cardBack.onClick { mNavController.navigateUp() }
    }
}