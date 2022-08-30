package com.example.iiifa_fan_android.ui.view.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.iiifa_fan_android.R
import com.example.iiifa_fan_android.data.pref.PreferencesHelper
import javax.inject.Inject


/**
 * Used for handle common methods of activities
 */
abstract class BaseActivity : AppCompatActivity() {
    @Inject
    internal lateinit var prefManager: PreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /**
     * Used to show hidden fragment
     */
    fun showFragment(fragment: Fragment, currentFragment: Fragment?): Fragment {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment)
        transaction.show(fragment)
        currentFragment?.let {
            if (fragment != currentFragment) transaction.hide(it)
        }
        try {
            transaction.commit()
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }
        return fragment
    }

    /**
     *used for hide show and add fragment
     */
    protected fun updateFragment(fragment: Fragment, currentFragment: Fragment?): Fragment {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.nav_host_fragment, fragment)
        currentFragment?.let {
            if (fragment != currentFragment) transaction.hide(it)
        }
        try {
            transaction.commit()
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }
        return fragment
    }

}