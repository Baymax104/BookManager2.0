package com.baymax104.bookmanager20.view

import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import com.baymax104.bookmanager20.BR
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.architecture.domain.State
import com.baymax104.bookmanager20.architecture.domain.StateHolder
import com.baymax104.bookmanager20.architecture.domain.activityViewModels
import com.baymax104.bookmanager20.architecture.view.BaseActivity
import com.baymax104.bookmanager20.architecture.view.DataBindingConfig
import com.baymax104.bookmanager20.databinding.ActivityMainBinding
import com.baymax104.bookmanager20.util.MainScope
import com.baymax104.bookmanager20.util.MainScopeContext
import com.baymax104.bookmanager20.util.start
import com.baymax104.bookmanager20.view.adapter.FragmentAdapter
import com.blankj.utilcode.util.ToastUtils
import com.drake.statusbar.immersive
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.cancel

class MainActivity : BaseActivity() {

    private val states: States by activityViewModels()

    class States : StateHolder() {
        val page = State(0)
        val fragments = listOf(ProcessFragment(), FinishFragment())
        val drawerOpen = State(false)
        val enterEdit = State(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainScope = lifecycleScope
        MainScopeContext = MainScope.coroutineContext
    }

    override fun configureBinding() = DataBindingConfig(R.layout.activity_main, BR.state, states)
        .add(
            BR.adapter to FragmentAdapter(this),
            BR.handler to Handler()
        )

    inner class Handler {
        val mainNavListener = NavigationBarView.OnItemSelectedListener {
            when (it.itemId) {
                R.id.progress -> states.page.value = 0
                R.id.finish -> states.page.value = 1
            }
            true
        }

        val leftNavItemListener = NavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.about -> ToastUtils.showShort("关于")
            }
            true
        }
    }

    override fun initUIComponent(binding: ViewDataBinding) {
        binding as ActivityMainBinding
        immersive(binding.toolbar, true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.leftNav.itemIconTintList = null
        binding.viewPager.offscreenPageLimit = 2
    }

    override fun createToolbarMenu(): Int = R.menu.toolbar

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> states.drawerOpen.value = true
            R.id.edit -> {
                states.enterEdit.value = true
                this@MainActivity start EditActivity::class
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        MainScope.cancel()
    }
}