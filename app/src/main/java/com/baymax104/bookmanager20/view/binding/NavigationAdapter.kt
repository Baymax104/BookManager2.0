package com.baymax104.bookmanager20.view.binding

import androidx.core.view.get
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/4 16:19
 *@Version 1
 */
@Suppress("unchecked_cast", "NotifyDataSetChanged", "SetTextI18n")
object NavigationAdapter {

    @JvmStatic
    @BindingAdapter("bottomNavigation_onItemSelected")
    fun BottomNavigationView.onItemSelectedListener(listener: NavigationBarView.OnItemSelectedListener) {
        setOnItemSelectedListener(listener)
    }

    @JvmStatic
    @BindingAdapter("bottomNavigation_page")
    fun BottomNavigationView.onPageChange(page: Int) {
        menu[page].isChecked = true
    }

    @JvmStatic
    @BindingAdapter("navigation_onItemSelected")
    fun NavigationView.onItemSelectedListener(listener: NavigationView.OnNavigationItemSelectedListener) {
        setNavigationItemSelectedListener(listener)
    }
}
