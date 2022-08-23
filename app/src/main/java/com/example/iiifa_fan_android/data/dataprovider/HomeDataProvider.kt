package com.example.iiifa_fan_android.data.dataprovider

import android.content.Context
import com.example.iiifa_fan_android.R

data class SideMenu(
    val tag: String,
    val menuTitle: String,
    val menuIcon: Int? = null,
    val subMenu: List<SideMenu> = arrayListOf(),
    val badgeCount: Int = 0
)

object HomeDataProvider {
    fun getMenuListWithoutLogin(context: Context): List<SideMenu>{
        return listOf(
            SideMenu("about_iifa_connect", context.getString(R.string.menu_about_iifa_connect), R.mipmap.ic_type_file,
                listOf(
                SideMenu("pp", context.getString(R.string.menu_pp)),
                SideMenu("tc", context.getString(R.string.menu_tc)),
                SideMenu("faq", context.getString(R.string.menu_faq)),
                SideMenu("cancellation_refund", context.getString(R.string.menu_cancellation_refund)),
                SideMenu("contact_us", context.getString(R.string.menu_contact_us)))),
            SideMenu("help_guide", context.getString(R.string.menu_help_guide), R.mipmap.ic_type_file),
            SideMenu("rate_us", context.getString(R.string.menu_rate_us), R.mipmap.ic_type_file)
        )
    }

    fun getMenuListWithLogin(context: Context): List<SideMenu>{
        return listOf(
            SideMenu("my_profile", context.getString(R.string.menu_my_profile), R.mipmap.ic_type_file,
                listOf(
                    SideMenu("edit_profile", context.getString(R.string.menu_edit_profile)),
                    SideMenu("change_password", context.getString(R.string.menu_change_password)))),
            SideMenu("my_shoutouts", context.getString(R.string.menu_my_shoutouts), R.mipmap.ic_type_folder),
            SideMenu("my_auditions", context.getString(R.string.menu_my_auditions), R.mipmap.ic_type_file,badgeCount = 2),
            SideMenu("account_management", context.getString(R.string.menu_account_management), R.mipmap.ic_type_folder,
                listOf(
                    SideMenu("app_design_setting", context.getString(R.string.menu_app_design_setting)),
                    SideMenu("my_preference", context.getString(R.string.menu_my_preference)))),
            SideMenu("payment", context.getString(R.string.menu_payment), R.mipmap.ic_type_file,
                listOf(
                    SideMenu("transaction_history", context.getString(R.string.menu_transaction_history),badgeCount = 3),
                    SideMenu("manage_payment_methods", context.getString(R.string.menu_manage_payment_methods)))),
            SideMenu("notification_setting", context.getString(R.string.menu_notification_setting), R.mipmap.ic_type_folder,badgeCount = 4),
            SideMenu("about_iifa_connect", context.getString(R.string.menu_about_iifa_connect), R.mipmap.ic_type_file,
                listOf(
                    SideMenu("pp", context.getString(R.string.menu_pp)),
                    SideMenu("tc", context.getString(R.string.menu_tc)),
                    SideMenu("faq", context.getString(R.string.menu_faq)),
                    SideMenu("cancellation_refund", context.getString(R.string.menu_cancellation_refund)),
                    SideMenu("contact_us", context.getString(R.string.menu_contact_us)))),
            SideMenu("help_guide", context.getString(R.string.menu_help_guide), R.mipmap.ic_type_folder),
            SideMenu("rate_us", context.getString(R.string.menu_rate_us), R.mipmap.ic_type_file),
            SideMenu("logout", context.getString(R.string.menu_logout), R.mipmap.ic_type_folder)
        )
    }

}