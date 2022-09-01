package com.example.iiifa_fan_android.ui.view.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.models.dataprovider.HomeDataProvider
import com.example.iiifa_fan_android.data.models.dataprovider.SideMenu
import com.example.iiifa_fan_android.databinding.ActivityMainDashboardBinding
import com.example.iiifa_fan_android.ui.view.base.BaseActivity
import com.example.iiifa_fan_android.ui.view.dashboard.adapter.SideMenuListAdapter
import com.example.iiifa_fan_android.ui.view.dashboard.cms.ContactusActivity
import com.example.iiifa_fan_android.ui.view.dashboard.cms.FAQActivity
import com.example.iiifa_fan_android.ui.view.dashboard.cms.PrivacyPolicyActivity
import com.example.iiifa_fan_android.ui.view.dashboard.fragments.HomeFragment
import com.example.iiifa_fan_android.ui.view.dashboard.myprofile.ChangePasswordActivity
import com.example.iiifa_fan_android.ui.view.dashboard.myprofile.EditProfileActivity
import com.example.iiifa_fan_android.ui.view.login.activities.LoginActivity
import com.example.iiifa_fan_android.utils.CustomFunctions
import com.example.iiifa_fan_android.utils.extensions.onClick
import com.example.iiifa_fan_android.utils.widget.MiddleDividerItemDecoration
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainDashboardActivity : BaseActivity(), SideMenuListAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainDashboardBinding
    private lateinit var sideMenuListAdapter : SideMenuListAdapter
    private var currentFragment: Fragment? = null
    private var currentPage = ""
    companion object {
        @JvmStatic
        fun getInstance(context: Context?) {
            Intent(context, MainDashboardActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                context?.startActivity(this)
            }
        }
    }

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
                FAQActivity.getInstance(this)
            }
            "cancellation_refund" -> {
                PrivacyPolicyActivity.getInstance(this,menuItem)
            }
            "contact_us" -> {
                ContactusActivity.getInstance(this)
            }
            "help_guide" -> {

            }
            "rate_us" -> {

            }
            "logout" -> {
                makeLogout()
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

    private fun makeLogout() {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialog)
        val sheetView: View = layoutInflater.inflate(R.layout.bottom_popup_exit_confimation, null)
        val btnYes: MaterialButton = sheetView.findViewById(R.id.btn_login)
        val btnNo: TextView = sheetView.findViewById(R.id.tv_no)
        val tv_title_botom_sheet: TextView = sheetView.findViewById(R.id.tv_title_botom_sheet)
        tv_title_botom_sheet.text = getString(R.string.sure_want_to_logout)

        btnYes.onClick {
            bottomSheetDialog.dismiss()
            CustomFunctions.handleForbiddenResponse()
        }

        btnNo.onClick { bottomSheetDialog.dismiss() }
        bottomSheetDialog.setContentView(sheetView)
        bottomSheetDialog.show()
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