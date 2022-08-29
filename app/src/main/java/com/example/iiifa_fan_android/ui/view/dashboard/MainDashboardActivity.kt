package com.example.iiifa_fan_android.ui.view.dashboard

import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.dataprovider.HomeDataProvider
import com.example.iiifa_fan_android.data.dataprovider.SideMenu
import com.example.iiifa_fan_android.databinding.ActivityMainDashboardBinding
import com.example.iiifa_fan_android.ui.view.base.BaseActivity
import com.example.iiifa_fan_android.ui.view.dashboard.adapter.SideMenuListAdapter
import com.example.iiifa_fan_android.ui.view.dashboard.fragments.HomeFragment
import com.example.iiifa_fan_android.ui.view.dashboard.myprofile.ChangePasswordActivity
import com.example.iiifa_fan_android.ui.view.dashboard.myprofile.EditProfileActivity
import com.example.iiifa_fan_android.utils.widget.MiddleDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainDashboardActivity : BaseActivity(), SideMenuListAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainDashboardBinding
    private lateinit var sideMenuListAdapter : SideMenuListAdapter
    private var currentFragment: Fragment? = null
    private var currentPage = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolBar()
        initViews()
        initListener()

    }

    private fun initToolBar() {
        setSupportActionBar(binding.appBarMainDashboard.toolbar)
        val actionBar = supportActionBar
        actionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.menu_drawer)
        }
    }

    private fun initViews() {
        val list = HomeDataProvider.getMenuListWithLogin(this)
        sideMenuListAdapter = SideMenuListAdapter(list,this)
        binding.rvMenu.adapter = sideMenuListAdapter
        val decoration = MiddleDividerItemDecoration(this,DividerItemDecoration.VERTICAL)
        decoration.setDividerColor(ContextCompat.getColor(this,R.color.white))
        binding.rvMenu.addItemDecoration(decoration)
    }

    private fun initListener() {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                binding.drawerLayout.openDrawer(GravityCompat.START)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    // side menu item click
    override fun onMenuItemClick(menuItem: SideMenu) {
//        binding.drawerLayout.closeDrawer(GravityCompat.START)
//        if (currentPage == menuItem.tag){
//            return
//        }
//        currentPage = menuItem.tag
//        supportActionBar?.title = menuItem.menuTitle
        when (menuItem.tag) {
            "edit_profile" -> {
                EditProfileActivity.getInstance(this)
            }
            "change_password" -> {
                ChangePasswordActivity.getInstance(this)
            }
            "my_shoutouts" -> {

            }
            "my_auditions" -> {

            }
            "app_design_setting" -> {

            }
            "my_preference" -> {

            }
            "transaction_history" -> {

            }
            "manage_payment_methods" -> {

            }
            "notification_setting" -> {

            }
            "pp" -> {
                PrivacyPolicyActivity.getInstance(this,menuItem)
            }
            "tc" -> {
                PrivacyPolicyActivity.getInstance(this,menuItem)
            }
            "faq" -> {

            }
            "cancellation_refund" -> {
                PrivacyPolicyActivity.getInstance(this,menuItem)
            }
            "contact_us" -> {

            }
            "help_guide" -> {

            }
            "rate_us" -> {

            }
            "logout" -> {

            }
            else ->{
                currentFragment?.let {
                    supportFragmentManager.beginTransaction().remove(it).commit()
                }
                val fragment = HomeFragment.newInstance()
                fragment.let {
                    currentFragment = updateFragment(it, currentFragment)
                }
            }
        }
    }

    override fun onBackPressed() {
        if (currentFragment is HomeFragment){
            super.onBackPressed()
        }else{
            currentPage = ""
            supportActionBar?.title = resources.getString(R.string.app_name)
            val fragment = HomeFragment.newInstance()
            fragment.let {
                currentFragment = showFragment(it, currentFragment)
            }
        }

    }
}