package com.baymax104.bookmanager20.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/18 23:31
 *@Version 1
 */
class FragmentAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    var fragments: List<Fragment> = listOf()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}