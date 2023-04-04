package com.baymax104.bookmanager20.architecture.view

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.baymax104.bookmanager20.R
import com.baymax104.bookmanager20.architecture.ActivityViewModelScope
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/3 15:39
 *@Version 1
 */
@AndroidEntryPoint
abstract class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelScope: ActivityViewModelScope

    private var mBinding: ViewDataBinding? = null

    protected abstract fun configureBinding(): DataBindingConfig

    protected open fun createToolbarMenu(): Int? = null

    protected open fun initUIComponent(binding: ViewDataBinding) {
    }

    protected open fun initViewModel() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Binding
        initViewModel()
        val config = configureBinding()
        val (layout, vmId, stateVM) = config

        val binding: ViewDataBinding = DataBindingUtil.setContentView(this, layout)
        binding.lifecycleOwner = this

        applyToolbar(binding.root)

        binding.setVariable(vmId, stateVM)
        config.params.forEach { key, value -> binding.setVariable(key, value) }
        mBinding = binding

        // initWindow
        initUIComponent(binding)
    }

    private fun applyToolbar(root: View) {
        val toolbar: Toolbar = root.findViewById(R.id.toolbar) ?: return
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuId = createToolbarMenu()
        return if (menuId != null) {
            menuInflater.inflate(menuId, menu)
            true
        } else {
            false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding?.unbind()
        mBinding = null
    }
}