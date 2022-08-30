package com.example.iiifa_fan_android.data.models.dataprovider

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
            SideMenu("about_iifa_connect", context.getString(R.string.menu_about_iifa_connect), R.drawable.menu_about,
                listOf(
                    SideMenu("pp", context.getString(R.string.menu_pp)),
                    SideMenu("tc", context.getString(R.string.menu_tc)),
                    SideMenu("faq", context.getString(R.string.menu_faq)),
                    SideMenu("cancellation_refund", context.getString(R.string.menu_cancellation_refund)),
                    SideMenu("contact_us", context.getString(R.string.menu_contact_us)))),
            SideMenu("help_guide", context.getString(R.string.menu_help_guide), R.drawable.menu_help_guide),
            SideMenu("rate_us", context.getString(R.string.menu_rate_us), R.drawable.menu_rate_us)
        )
    }

    fun getMenuListWithLogin(context: Context): List<SideMenu>{
        return listOf(
            SideMenu("my_profile", context.getString(R.string.menu_my_profile), R.drawable.menu_my_profile,
                listOf(
                    SideMenu("edit_profile", context.getString(R.string.menu_edit_profile)),
                    SideMenu("change_password", context.getString(R.string.menu_change_password)))),
            SideMenu("my_shoutouts", context.getString(R.string.menu_my_shoutouts), R.mipmap.ic_type_folder),
            SideMenu("my_auditions", context.getString(R.string.menu_my_auditions), R.mipmap.ic_type_file,badgeCount = 2),
            SideMenu("my_preference", context.getString(R.string.menu_my_preference), R.drawable.menu_my_preferences),
            SideMenu("payment", context.getString(R.string.menu_payment), R.drawable.menu_payment,
                listOf(
                    SideMenu("transaction_history", context.getString(R.string.menu_transaction_history),badgeCount = 3),
                    SideMenu("manage_payment_methods", context.getString(R.string.menu_manage_payment_methods)))),
            SideMenu("notification_setting", context.getString(R.string.menu_notification_setting), R.drawable.menu_notification,badgeCount = 4),
            SideMenu("about_iifa_connect", context.getString(R.string.menu_about_iifa_connect), R.drawable.menu_about,
                listOf(
                    SideMenu("pp", context.getString(R.string.menu_pp)),
                    SideMenu("tc", context.getString(R.string.menu_tc)),
                    SideMenu("faq", context.getString(R.string.menu_faq)),
                    SideMenu("cancellation_refund", context.getString(R.string.menu_cancellation_refund)),
                    SideMenu("contact_us", context.getString(R.string.menu_contact_us)))),
            SideMenu("help_guide", context.getString(R.string.menu_help_guide), R.drawable.menu_help_guide),
            SideMenu("rate_us", context.getString(R.string.menu_rate_us), R.drawable.menu_rate_us),
            SideMenu("logout", context.getString(R.string.menu_logout), R.drawable.menu_logout)
        )
    }

}