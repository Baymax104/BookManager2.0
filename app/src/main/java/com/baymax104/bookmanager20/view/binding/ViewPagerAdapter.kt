package com.baymax104.bookmanager20.view.binding

import androidx.core.view.get
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.baymax104.bookmanager20.adapter.FragmentAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/4 16:20
 *@Version 1
 */
object ViewPagerAdapter {

    @JvmStatic
    @BindingAdapter("viewPager_adapter", "viewPager_fragments")
    fun ViewPager2.fragmentAdapter(adapter: FragmentAdapter, fragments: List<Fragment>) {
        adapter.fragments = fragments
        this.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("viewPager_page")
    fun ViewPager2.onPageChange(page: Int) {
        currentItem = page
    }

    @JvmStatic
    @BindingAdapter("viewPager_bindNavPageChange")
    fun ViewPager2.onPageChangeCallback(navigationView: BottomNavigationView) {
        registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                navigationView.menu[position].isChecked = true
            }
        })
    }

}