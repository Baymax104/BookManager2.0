package com.baymax104.bookmanager20.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.MenuProvider
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.adapter.FragmentAdapter
import com.baymax104.bookmanager20.databinding.ActivityMainBinding
import com.baymax104.bookmanager20.util.MainScope
import com.baymax104.bookmanager20.viewModel.MainViewModel
import com.blankj.utilcode.util.ToastUtils
import com.drake.statusbar.immersive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = vm
        binding.adapter = FragmentAdapter(this)

        initWindow()

        vm.page.observe(this) {
            binding.nav.menu[it].isChecked = true
            binding.viewPager.currentItem = it
        }

        binding.nav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.progress -> vm.page.value = 0
                R.id.finish -> vm.page.value = 1
            }
            true
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.nav.menu[position].isChecked = true
            }
        })

        binding.leftNav.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.about -> ToastUtils.showShort("关于")
            }
            true
        }

    }

    private fun initWindow() {
        immersive(binding.toolBar, true)
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolBar.setNavigationIcon(R.drawable.left_nav)
        binding.leftNav.itemIconTintList = null
        val menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) =
                menuInflater.inflate(R.menu.toolbar, menu)

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
                if (menuItem.itemId == android.R.id.home) {
                    binding.drawer.openDrawer(GravityCompat.START)
                    true
                } else {
                    true
                }
        }
        addMenuProvider(menuProvider, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        MainScope.cancel()
    }
}