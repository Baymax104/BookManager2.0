package com.baymax104.bookmanager20.view

import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.baymax104.bookmanager20.BR
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.adapter.FragmentAdapter
import com.baymax104.bookmanager20.architecture.view.BaseActivity
import com.baymax104.bookmanager20.architecture.view.DataBindingConfig
import com.baymax104.bookmanager20.databinding.ActivityMainBinding
import com.baymax104.bookmanager20.util.MData
import com.baymax104.bookmanager20.util.MainScope
import com.baymax104.bookmanager20.util.MainScopeContext
import com.baymax104.bookmanager20.view.finish.FinishFragment
import com.baymax104.bookmanager20.view.process.ProcessFragment
import com.blankj.utilcode.util.ToastUtils
import com.drake.statusbar.immersive
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    object State : ViewModel() {
        val page: MData<Int> = MData(0)
        val fragments = listOf(ProcessFragment(), FinishFragment())
        val drawerOpen: MData<Boolean> = MData(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainScope = lifecycleScope
        MainScopeContext = MainScope.coroutineContext

        State.drawerOpen.observe(this) {
            ToastUtils.showShort(it.toString())
        }
    }

    inner class Handler {

        val mainNavListener = NavigationBarView.OnItemSelectedListener {
            when (it.itemId) {
                R.id.progress -> State.page.value = 0
                R.id.finish -> State.page.value = 1
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

    override fun configureBinding() = DataBindingConfig(R.layout.activity_main, BR.state, State)
        .add(
            BR.adapter to FragmentAdapter(this),
            BR.handler to Handler()
        )

    override fun initUIComponent(binding: ViewDataBinding) {
        binding as ActivityMainBinding
        immersive(binding.toolbar, true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.leftNav.itemIconTintList = null
    }

    override fun createToolbarMenu(): Int = R.menu.toolbar

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> State.drawerOpen.value = true
            R.id.edit -> EditActivity.actionStart(this@MainActivity)
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        MainScope.cancel()
    }
}