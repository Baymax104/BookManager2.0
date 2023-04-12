package com.baymax104.bookmanager20.architecture.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

/**
 *@Description
 *@Author John
 *@email
 *@Date 2023/4/3 18:16
 *@Version 1
 */
abstract class BaseFragment : Fragment() {

    protected lateinit var activity: AppCompatActivity

    private var mBinding: ViewDataBinding? = null

    protected abstract fun configureBinding(): DataBindingConfig

    protected open fun initUIComponent(binding: ViewDataBinding) {
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // initialize
        val config = configureBinding()
        val (layout, vmId, state) = config
        val binding: ViewDataBinding = DataBindingUtil.inflate(inflater, layout, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        // binding
        binding.setVariable(vmId, state)
        config.params.forEach { variableId, value ->
            binding.setVariable(variableId, value)
        }
        mBinding = binding

        // initUI
        initUIComponent(binding)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding?.unbind()
        mBinding = null
    }
}