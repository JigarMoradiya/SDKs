package com.connectedminds.sdks.ui.view.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback

import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.connectedminds.sdks.R
import com.connectedminds.sdks.databinding.ActivityLoginDashboardBinding
import com.connectedminds.sdks.ui.view.base.BaseActivity
import com.connectedminds.sdks.utils.extensions.hide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : BaseActivity(){
    lateinit var navController: NavController
    lateinit var navHostFragment: NavHostFragment
    var selectedFragment: Int = -1
    lateinit var binding: ActivityLoginDashboardBinding
    companion object {
        fun getInstance(context: Context?) {
            Intent(context, HomeActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                context?.startActivity(this)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolBar()
        initViews()
        initListener()
        initObserver()
    }

    private fun initObserver() {

    }

    private fun setNavigationGraph() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, _ ->
            selectedFragment = destination.id
            showToolbarTitle(destination.id)
        }
    }
    private fun initToolBar() {
        setSupportActionBar(binding.toolbar)
    }

    private fun initViews() {
        setNavigationGraph()
        onMainActivityBack()
    }

    private fun initListener() {

    }
    private fun showToolbarTitle(id: Int) {
        when (id) {
            R.id.homeFragment -> {
                binding.toolbar.hide()
                binding.relMain.hide()
                showBackArrow()
            }
            else ->{
                lifecycleScope.launch {
                    delay(100)
                    binding.toolbar.hide()
                    binding.relMain.hide()
                }
            }
        }
    }
    private fun showBackArrow() { //  Hide bottom navigation bar, Show toolbar back icon
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
    }

    private fun onMainActivityBack() {
        onBackPressedDispatcher.addCallback(
            this, // lifecycle owner
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    fragmentBackClick()
                }
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                fragmentBackClick()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun fragmentBackClick() {
        when (selectedFragment) {
            R.id.homeFragment -> {
                finish()
            }
            else -> {
                navigationUp()
            }
        }
    }
    private fun navigationUp() {
        navController.navigateUp()
    }
}