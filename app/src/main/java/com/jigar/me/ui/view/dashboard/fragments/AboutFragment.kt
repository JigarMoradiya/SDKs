package com.jigar.me.ui.view.dashboard.fragments

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.jigar.me.R
import com.jigar.me.databinding.FragmentAboutUsBinding
import com.jigar.me.ui.view.base.BaseFragment
import com.jigar.me.utils.extensions.onClick
import com.jigar.me.utils.extensions.openMail
import com.jigar.me.utils.extensions.openPlayStore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment : BaseFragment() {
    private lateinit var binding: FragmentAboutUsBinding
    private lateinit var mNavController: NavController
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        setNavigationGraph()
        initViews()
        initListener()
        return binding.root
    }
    private fun setNavigationGraph() {
        mNavController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }
    private fun initViews() {
        try {
            val pInfo =
                requireContext().packageManager.getPackageInfo(requireContext().packageName, 0)
            val version = pInfo.versionName
            binding.txtVersion.text = "${getString(R.string.version)} $version"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun initListener() {
        binding.cardBack.onClick { mNavController.navigateUp() }
        binding.txtRateUs.onClick { requireContext().openPlayStore() }
        binding.txtNeedHelp.onClick { requireContext().openMail() }
        binding.txtPrivacy.onClick { mNavController.navigate(R.id.action_aboutFragment_to_privacyPolicyFragment)}
    }
}