package com.example.iiifa_fan_android.ui.view.dashboard

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.dataprovider.HomeDataProvider
import com.example.iiifa_fan_android.databinding.ActivityMainDashboardBinding
import com.example.iiifa_fan_android.ui.view.dashboard.adapter.SideMenuListAdapter
import com.example.iiifa_fan_android.utils.widget.MiddleDividerItemDecoration

class MainDashboardActivity : AppCompatActivity(), SideMenuListAdapter.OnItemClickListener {

    private lateinit var binding: ActivityMainDashboardBinding
    private lateinit var sideMenuListAdapter : SideMenuListAdapter
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
            setHomeAsUpIndicator(R.drawable.ic_done)
        }
    }

    private fun initViews() {
        val list = HomeDataProvider.getMenuListWithLogin(this)
        sideMenuListAdapter = SideMenuListAdapter(list,this)
        binding.rvMenu.adapter = sideMenuListAdapter
        val decoration = MiddleDividerItemDecoration(this,DividerItemDecoration.VERTICAL)
        decoration.setDividerColor(ContextCompat.getColor(this,R.color.white70))
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
    override fun onMenuItemClick(itemTag: String) {
        when (itemTag) {
            "edit_profile" -> {

            }
            "change_password" -> {

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

            }
            "tc" -> {

            }
            "faq" -> {

            }
            "cancellation_refund" -> {

            }
            "contact_us" -> {

            }
            "help_guide" -> {

            }
            "rate_us" -> {

            }
            "logout" -> {

            }
        }
    }

}