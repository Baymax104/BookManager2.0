package com.baymax104.bookmanager20.view.binding

import android.view.View
import androidx.core.view.GravityCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import java.util.*

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/3/18 23:41
 *@Version 1
 */
@Suppress("unchecked_cast", "NotifyDataSetChanged", "SetTextI18n")
object DrawerAdapter {

    @JvmStatic
    @BindingAdapter("drawer_open")
    fun DrawerLayout.open(open: Boolean) {
        when {
            open && !isOpen -> openDrawer(GravityCompat.START)
            !open && isOpen -> closeDrawer(GravityCompat.START)
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "drawer_open", event = "drawer_openAttrChanged")
    fun DrawerLayout.isOpened(): Boolean {
        return isOpen
    }

    @JvmStatic
    @BindingAdapter("drawer_openAttrChanged")
    fun DrawerLayout.onStateChanged(listener: InverseBindingListener) {
        addDrawerListener(object : SimpleDrawerListener() {

            // 展开完成时调用，不能使用onDrawerStateChanged代替
            override fun onDrawerOpened(drawerView: View) {
                listener.onChange()
            }

            override fun onDrawerClosed(drawerView: View) {
                listener.onChange()
            }
        })
    }

}


