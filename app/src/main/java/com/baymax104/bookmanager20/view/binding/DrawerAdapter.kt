package com.baymax104.bookmanager20.view.binding

import android.view.View
import androidx.core.view.GravityCompat
import androidx.databinding.BindingAdapter
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import com.baymax104.bookmanager20.architecture.domain.State
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
    @BindingAdapter("drawer_stateChanged")
    fun DrawerLayout.onStateChanged(state: State<Boolean>) {
        addDrawerListener(object : SimpleDrawerListener() {
            override fun onDrawerOpened(drawerView: View) {
                state.value = true
            }

            override fun onDrawerClosed(drawerView: View) {
                state.value = false
            }
        })
    }

}


